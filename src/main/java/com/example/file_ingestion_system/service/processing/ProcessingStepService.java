package com.example.file_ingestion_system.service.processing;

import com.example.file_ingestion_system.model.dto.ProcessingStepDto;
import com.example.file_ingestion_system.mapper.ProcessingStepMapper;
import com.example.file_ingestion_system.model.entity.ProcessingStep;
import com.example.file_ingestion_system.repository.ProcessingStepRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProcessingStepService {

    private final ProcessingStepRepository processingStepRepository;
    private final ProcessingStepMapper processingStepMapper;

    public ProcessingStepService(ProcessingStepRepository processingStepRepository,
                                ProcessingStepMapper processingStepMapper) {
        this.processingStepRepository = processingStepRepository;
        this.processingStepMapper = processingStepMapper;
    }

    public List<ProcessingStepDto> getStepsForJob(Long jobId) {
        return processingStepRepository.findByFileJobIdOrderByStartTime(jobId)
            .stream()
            .map(processingStepMapper::toDto)
            .collect(Collectors.toList());
    }

    public ProcessingStepDto getStepById(Long stepId) {
        ProcessingStep step = processingStepRepository.findById(stepId)
            .orElseThrow(() -> new RuntimeException("Processing step not found"));
        return processingStepMapper.toDto(step);
    }

    public ProcessingStepDto retryStep(Long stepId) {
        ProcessingStep step = processingStepRepository.findById(stepId)
            .orElseThrow(() -> new RuntimeException("Processing step not found"));
        
        // Reset step status for retry
        step.setStatus("PENDING");
        step.setStartTime(null);
        step.setEndTime(null);
        step.setDetails(null);
        
        step = processingStepRepository.save(step);
        return processingStepMapper.toDto(step);
    }
}