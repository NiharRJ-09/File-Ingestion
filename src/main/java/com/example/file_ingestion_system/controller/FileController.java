package com.example.file_ingestion_system.controller;

import com.example.file_ingestion_system.mapper.FileJobMapper;
import com.example.file_ingestion_system.model.dto.FileJobDto;
import com.example.file_ingestion_system.model.entity.FileJob;
import com.example.file_ingestion_system.model.entity.User;
import com.example.file_ingestion_system.service.job.JobManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@Tag(name = "File Management", description = "File upload and processing endpoints")
@SecurityRequirement(name = "bearerAuth")
public class FileController {

    private final JobManagementService jobManagementService;
    private final FileJobMapper fileJobMapper;

    public FileController(
            JobManagementService jobManagementService,
            FileJobMapper fileJobMapper) {
        this.jobManagementService = jobManagementService;
        this.fileJobMapper = fileJobMapper;
    }

    @Operation(
            summary = "Upload a file for processing",
            description = "Upload a file to be processed by the system. Supported formats: CSV, JSON, Excel",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "File uploaded successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = FileJobDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid file or parameters"
                    ),
                    @ApiResponse(
                            responseCode = "415",
                            description = "Unsupported file type"
                    )
            }
    )
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public FileJobDto uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") String fileType,
            @RequestParam(value = "validationConfigId", required = false) Long validationConfigId,
            @AuthenticationPrincipal User user) throws Exception {

        FileJob fileJob = jobManagementService.createFileJob(file, fileType, user);
        return fileJobMapper.toDto(fileJob);
    }
}
