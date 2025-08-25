package com.example.file_ingestion_system.service.transformation;

import com.example.file_ingestion_system.exception.ResourceNotFoundException;
import com.example.file_ingestion_system.model.dto.TransformationDto;
import com.example.file_ingestion_system.model.entity.FileJob;
import com.example.file_ingestion_system.model.entity.ProcessingStep;
import com.example.file_ingestion_system.model.entity.Transformation;
import com.example.file_ingestion_system.repository.ProcessingStepRepository;
import com.example.file_ingestion_system.repository.TransformationRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransformationService {

    private final Map<String, com.example.file_ingestion_system.service.transformation.Transformation> transformers;
    private final TransformationRepository transformationRepository;
    private final ObjectMapper objectMapper;
    private final ProcessingStepRepository processingStepRepository;

    public TransformationService(
            Set<com.example.file_ingestion_system.service.transformation.Transformation> transformerSet,
            TransformationRepository transformationRepository,
            ObjectMapper objectMapper, ProcessingStepRepository processingStepRepository) {
        this.processingStepRepository = processingStepRepository;
        this.transformers = new HashMap<>();
        transformerSet.forEach(transformer ->
                transformers.put(transformer.getTransformType(), transformer));
        this.transformationRepository = transformationRepository;
        this.objectMapper = objectMapper;
    }

    public ProcessingStep applyTransformations(FileJob fileJob, List<Map<String, Object>> data) {
        ProcessingStep transformationStep = new ProcessingStep();
        transformationStep.setFileJob(fileJob);
        transformationStep.setStepType("TRANSFORMATION");
        transformationStep.setStatus("IN_PROGRESS");
        transformationStep.setStartTime(LocalDateTime.now());

        try {
            List<Transformation> transformations = transformationRepository
                    .findByStepIdOrderByTransformOrder(fileJob.getId());

            for (Map<String, Object> row : data) {
                for (Transformation transformation : transformations) {
                    Map<String, Object> config = objectMapper.readValue(
                            transformation.getTransformConfig(),
                            new TypeReference<Map<String, Object>>() {});

                    com.example.file_ingestion_system.service.transformation.Transformation transformer =
                            transformers.get(transformation.getTransformType());

                    if (transformer != null) {
                        transformer.transform(row, config);
                    }
                }
            }

            transformationStep.setStatus("COMPLETED");
            transformationStep.setDetails("{\"transformedRecords\": " + data.size() + "}");

        } catch (Exception e) {
            transformationStep.setStatus("FAILED");
            transformationStep.setDetails("{\"error\": \"" + e.getMessage() + "\"}");
        } finally {
            transformationStep.setEndTime(LocalDateTime.now());
            fileJob.getProcessingSteps().add(transformationStep);
        }

        return transformationStep;
    }

    public TransformationDto createTransformation(@Valid TransformationDto transformationDto) {
        Transformation transformation = new Transformation();
        transformation.setTransformType(transformationDto.getTransformType());
        transformation.setSourceField(transformationDto.getSourceField());
        transformation.setTargetField(transformationDto.getTargetField());
        transformation.setTransformConfig(transformationDto.getTransformConfig());
        transformation.setTransformOrder(transformationDto.getTransformOrder());

        // Set processing step if stepId is provided
        if (transformationDto.getStepId() != null) {
            ProcessingStep step = processingStepRepository.findById(transformationDto.getStepId())
                    .orElseThrow(() -> new ResourceNotFoundException("ProcessingStep", "id", transformationDto.getStepId()));
            transformation.setProcessingStep(step);
        }

        Transformation savedTransformation = transformationRepository.save(transformation);
        return mapToDto(savedTransformation);
    }

    public List<TransformationDto> getTransformationsForStep(Long stepId) {
        List<Transformation> transformations = transformationRepository
                .findByProcessingStepIdOrderByTransformOrder(stepId);
        return transformations.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public TransformationDto updateTransformation(Long transformationId, @Valid TransformationDto transformationDto) {
        Transformation transformation = transformationRepository.findById(transformationId)
                .orElseThrow(() -> new ResourceNotFoundException("Transformation", "id", transformationId));

        transformation.setTransformType(transformationDto.getTransformType());
        transformation.setSourceField(transformationDto.getSourceField());
        transformation.setTargetField(transformationDto.getTargetField());
        transformation.setTransformConfig(transformationDto.getTransformConfig());
        transformation.setTransformOrder(transformationDto.getTransformOrder());

        Transformation updatedTransformation = transformationRepository.save(transformation);
        return mapToDto(updatedTransformation);
    }

    private TransformationDto mapToDto(Transformation transformation) {
        TransformationDto dto = new TransformationDto();
        dto.setId(transformation.getId());
        dto.setStepId(transformation.getProcessingStep() != null ?
                transformation.getProcessingStep().getId() : null);
        dto.setTransformType(transformation.getTransformType());
        dto.setSourceField(transformation.getSourceField());
        dto.setTargetField(transformation.getTargetField());
        dto.setTransformConfig(transformation.getTransformConfig());
        dto.setTransformOrder(transformation.getTransformOrder());
        return dto;
    }

    public void deleteTransformation(Long transformationId) {
        if (!transformationRepository.existsById(transformationId)) {
            throw new ResourceNotFoundException("Transformation", "id", transformationId);
        }
        transformationRepository.deleteById(transformationId);
    }
}
