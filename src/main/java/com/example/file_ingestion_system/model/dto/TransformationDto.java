package com.example.file_ingestion_system.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransformationDto {
    private Long id;
    private Long stepId;
    
    @NotBlank(message = "Transform type is required")
    private String transformType;
    
    private String sourceField;
    private String targetField;
    private String transformConfig;
    
    @NotNull(message = "Transform order is required")
    private Integer transformOrder;
}