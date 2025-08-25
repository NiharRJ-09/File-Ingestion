package com.example.file_ingestion_system.mapper;

import com.example.file_ingestion_system.model.dto.ProcessingStepDto;
import com.example.file_ingestion_system.model.entity.ProcessingStep;
import org.springframework.stereotype.Component;

@Component
public class ProcessingStepMapper {

    public ProcessingStepDto toDto(ProcessingStep step) {
        if (step == null) {
            return null;
        }

        ProcessingStepDto dto = new ProcessingStepDto();
        dto.setId(step.getId());
        dto.setStepType(step.getStepType());
        dto.setStartTime(step.getStartTime());
        dto.setEndTime(step.getEndTime());
        dto.setStatus(step.getStatus());
        dto.setDetails(step.getDetails());

        return dto;
    }
}