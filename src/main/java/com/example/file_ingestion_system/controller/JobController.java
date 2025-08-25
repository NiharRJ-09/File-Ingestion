package com.example.file_ingestion_system.controller;

import com.example.file_ingestion_system.model.dto.FileJobDto;
import com.example.file_ingestion_system.mapper.FileJobMapper;
import com.example.file_ingestion_system.model.entity.FileJob;
import com.example.file_ingestion_system.model.entity.User;
import com.example.file_ingestion_system.service.job.JobManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@Tag(name = "Job Management", description = "File job management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class JobController {

    private final JobManagementService jobManagementService;
    private final FileJobMapper fileJobMapper;

    public JobController(JobManagementService jobManagementService, FileJobMapper fileJobMapper) {
        this.jobManagementService = jobManagementService;
        this.fileJobMapper = fileJobMapper;
    }

    @Operation(summary = "Get job by ID")
    @GetMapping("/{jobId}")
    public FileJobDto getJob(@PathVariable Long jobId) {
        FileJob job = jobManagementService.getJobById(jobId);
        return fileJobMapper.toDto(job);
    }

    @Operation(summary = "Get user's jobs")
    @GetMapping("/my-jobs")
    public Page<FileJobDto> getUserJobs(@AuthenticationPrincipal User user, Pageable pageable) {
        return jobManagementService.getUserJobs(user, pageable)
                .map(fileJobMapper::toDto);
    }

    @Operation(summary = "Get all jobs (admin only)")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<FileJobDto> getAllJobs(Pageable pageable) {
        return jobManagementService.getAllJobs(pageable)
                .map(fileJobMapper::toDto);
    }

    @Operation(summary = "Get jobs by status")
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<FileJobDto> getJobsByStatus(@PathVariable String status, Pageable pageable) {
        return jobManagementService.getJobsByStatus(status, pageable)
                .map(fileJobMapper::toDto);
    }
}