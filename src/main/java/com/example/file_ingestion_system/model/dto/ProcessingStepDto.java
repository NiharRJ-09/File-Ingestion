package com.example.file_ingestion_system.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProcessingStepDto {
    private Long id;
    private String stepType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String details;
}
