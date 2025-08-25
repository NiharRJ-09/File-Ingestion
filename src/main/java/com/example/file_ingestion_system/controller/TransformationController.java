package com.example.file_ingestion_system.controller;

import com.example.file_ingestion_system.model.dto.TransformationDto;
import com.example.file_ingestion_system.service.transformation.TransformationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/transformations")
@Tag(name = "Transformations", description = "Data transformation management")
@SecurityRequirement(name = "bearerAuth")
public class TransformationController {

    private final TransformationService transformationService;

    public TransformationController(TransformationService transformationService) {
        this.transformationService = transformationService;
    }

    @Operation(summary = "Create transformation")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransformationDto createTransformation(@Valid @RequestBody TransformationDto transformationDto) {
        return transformationService.createTransformation(transformationDto);
    }

    @Operation(summary = "Get transformations for processing step")
    @GetMapping("/step/{stepId}")
    public List<TransformationDto> getTransformationsForStep(@PathVariable Long stepId) {
        return transformationService.getTransformationsForStep(stepId);
    }

    @Operation(summary = "Update transformation")
    @PutMapping("/{transformationId}")
    public TransformationDto updateTransformation(@PathVariable Long transformationId, 
                                                 @Valid @RequestBody TransformationDto transformationDto) {
        return transformationService.updateTransformation(transformationId, transformationDto);
    }

    @Operation(summary = "Delete transformation")
    @DeleteMapping("/{transformationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransformation(@PathVariable Long transformationId) {
        transformationService.deleteTransformation(transformationId);
    }
}