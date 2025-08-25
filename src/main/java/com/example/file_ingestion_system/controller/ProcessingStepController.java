package com.example.file_ingestion_system.controller;

import com.example.file_ingestion_system.model.dto.ProcessingStepDto;
import com.example.file_ingestion_system.service.processing.ProcessingStepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/processing-steps")
@Tag(name = "Processing Steps", description = "File processing step management")
@SecurityRequirement(name = "bearerAuth")
public class ProcessingStepController {

    private final ProcessingStepService processingStepService;

    public ProcessingStepController(ProcessingStepService processingStepService) {
        this.processingStepService = processingStepService;
    }

    @Operation(summary = "Get processing steps for job")
    @GetMapping("/job/{jobId}")
    public List<ProcessingStepDto> getStepsForJob(@PathVariable Long jobId) {
        return processingStepService.getStepsForJob(jobId);
    }

    @Operation(summary = "Get processing step by ID")
    @GetMapping("/{stepId}")
    public ProcessingStepDto getStepById(@PathVariable Long stepId) {
        return processingStepService.getStepById(stepId);
    }

    @Operation(summary = "Retry failed processing step")
    @PostMapping("/{stepId}/retry")
    public ProcessingStepDto retryStep(@PathVariable Long stepId) {
        return processingStepService.retryStep(stepId);
    }
}