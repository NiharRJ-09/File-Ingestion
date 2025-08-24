package com.example.file_ingestion_system.service.file.processor;
import com.example.file_ingestion_system.model.entity.FileJob;
import java.nio.file.Path;
import java.util.Map;
import java.util.List;
public interface FileProcessor {
    String getSupportedFileType();
    List<Map<String, Object>> parseFile(Path filePath) throws Exception;
    void processFile(FileJob fileJob) throws Exception;
}
