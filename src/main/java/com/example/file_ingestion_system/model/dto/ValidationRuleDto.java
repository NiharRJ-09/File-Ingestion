package com.example.file_ingestion_system.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ValidationRuleDto {
    private Long id;
    private Long jobId;

    @NotBlank(message = "Rule name is mandatory")
    private String rulename;

    @NotBlank(message = "Rule type is mandatory")
    private String ruleType;

    @NotBlank(message = "Rule config is mandatory")
    private String ruleConfig;

    @NotBlank(message = "Severity is mandatory")
    private String severity;

    private LocalDateTime createdAt;
}
