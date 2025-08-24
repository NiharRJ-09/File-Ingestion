package com.example.file_ingestion_system.service.file.processor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class FileProcessorFactory {
    private final Map<String, FileProcessor> processors;

    public FileProcessorFactory(Set<FileProcessor> processorSet) {
        processors = new HashMap<>();
        processorSet.forEach(processor ->
                processors.put(processor.getSupportedFileType(), processor));
    }

    public FileProcessor getProcessor(String fileType) {
        FileProcessor processor = processors.get(fileType);
        if (processor == null) {
            throw new UnsupportedOperationException("No processor available for file type: " + fileType);
        }
        return processor;
    }
}
