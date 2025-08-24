package com.example.file_ingestion_system.service.transformation;

import com.example.file_ingestion_system.model.entity.FileJob;
import com.example.file_ingestion_system.model.entity.ProcessingStep;
import com.example.file_ingestion_system.model.entity.Transformation;
import com.example.file_ingestion_system.repository.TransformationRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TransformationService {

    private final Map<String, com.example.file_ingestion_system.service.transformation.Transformation> transformers;
    private final TransformationRepository transformationRepository;
    private final ObjectMapper objectMapper;

    public TransformationService(
            Set<com.example.file_ingestion_system.service.transformation.Transformation> transformerSet,
            TransformationRepository transformationRepository,
            ObjectMapper objectMapper) {
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
}
