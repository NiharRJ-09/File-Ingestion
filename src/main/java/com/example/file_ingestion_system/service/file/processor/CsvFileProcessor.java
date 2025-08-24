package com.example.file_ingestion_system.service.file.processor;


import com.example.file_ingestion_system.model.entity.FileJob;
import com.example.file_ingestion_system.model.entity.ProcessingStep;
import com.example.file_ingestion_system.service.file.FileStorageService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CsvFileProcessor implements FileProcessor{
    private final FileStorageService fileStorageService;

    public CsvFileProcessor(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @Override
    public String getSupportedFileType() {
        return "CSV";
    }

    @Override
    public List<Map<String, Object>> parseFile(Path filePath) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord record : csvParser) {
                Map<String, Object> row = new HashMap<>();
                csvParser.getHeaderNames().forEach(header ->
                        row.put(header, record.get(header)));
                result.add(row);
            }
        }

        return result;
    }

    @Override
    public void processFile(FileJob fileJob) throws Exception {
        ProcessingStep parseStep = new ProcessingStep();
        parseStep.setFileJob(fileJob);
        parseStep.setStepType("PARSE");
        parseStep.setStatus("IN_PROGRESS");
        parseStep.setStartTime(LocalDateTime.now());

        try {
            Path filePath = fileStorageService.getFilePath(fileJob.getFilename());
            List<Map<String, Object>> parsedData = parseFile(filePath);

            // Store parsing results or pass to next step
            parseStep.setStatus("COMPLETED");
            parseStep.setEndTime(LocalDateTime.now());

            // Additional processing steps would be added here

        } catch (Exception e) {
            parseStep.setStatus("FAILED");
            parseStep.setEndTime(LocalDateTime.now());
            parseStep.setDetails("{\"error\": \"" + e.getMessage() + "\"}");
            throw e;
        } finally {
            fileJob.getProcessingSteps().add(parseStep);
        }
    }
}
