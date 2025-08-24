package com.example.file_ingestion_system.model.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FileJobDto {
    private Long id;
    private String username;
    private String filename;
    private String originalFilename;
    private String fileType;
    private LocalDateTime uploadDate;
    private String status;
    private String errorMessage;
    private String resultLocation;
    private List<ProcessingStepDto> processingSteps;
}
