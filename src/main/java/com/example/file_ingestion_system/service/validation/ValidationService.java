package com.example.file_ingestion_system.service.validation;
import com.example.file_ingestion_system.exception.ResourceNotFoundException;
import com.example.file_ingestion_system.model.dto.ValidationRuleDto;
import com.example.file_ingestion_system.model.entity.FileJob;
import com.example.file_ingestion_system.model.entity.ProcessingStep;
import com.example.file_ingestion_system.model.entity.ValidationRule;
import com.example.file_ingestion_system.repository.ValidationRuleRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ValidationService {
    private final Map<String, ValidationRule> validationRules;
    private final ValidationRuleRepository validationRuleRepository;
    private final ObjectMapper objectMapper;

    public ValidationService(
            Set<ValidationRule> validationRuleSet,
            ValidationRuleRepository validationRuleRepository,
            ObjectMapper objectMapper) {
        this.validationRules = new HashMap<>();
        validationRuleSet.forEach(rule ->
                validationRules.put(rule.getRuleName(), rule));
        this.validationRuleRepository = validationRuleRepository;
        this.objectMapper = objectMapper;
    }

    public ProcessingStep validateData(FileJob fileJob, List<Map<String, Object>> data) {
        ProcessingStep validationStep = new ProcessingStep();
        validationStep.setFileJob(fileJob);
        validationStep.setStepType("VALIDATION");
        validationStep.setStatus("IN_PROGRESS");
        validationStep.setStartTime(LocalDateTime.now());

        try {
            List<ValidationRule> rules = validationRuleRepository.findByJobId(fileJob.getId());

            Map<String, Object> validationResults = new HashMap<>();
            List<Map<String, Object>> validationErrors = new ArrayList<>();

            for (Map<String, Object> row : data) {
                for (ValidationRule rule : rules) {
                    Map<String, Object> ruleConfig = objectMapper.readValue(
                            rule.getRuleConfig(),
                            new TypeReference<Map<String, Object>>() {});

                    ValidationRule validationRule = validationRules.get(rule.getRuleName());
                    if (validationRule == null) {
                        continue;
                    }

                    if (!validationRule.validate(row, ruleConfig)) {
                        Map<String, Object> error = new HashMap<>();
                        error.put("ruleName", rule.getRuleName());
                        error.put("message", validationRule.getErrorMessage(ruleConfig));
                        error.put("severity", rule.getSeverity());
                        error.put("rowData", row);
                        validationErrors.add(error);
                    }
                }
            }

            validationResults.put("totalRecords", data.size());
            validationResults.put("errors", validationErrors);
            validationResults.put("errorCount", validationErrors.size());

            validationStep.setDetails(objectMapper.writeValueAsString(validationResults));

            if (validationErrors.isEmpty()) {
                validationStep.setStatus("COMPLETED");
            } else {
                // Handle validation errors based on severity
                boolean hasCriticalErrors = validationErrors.stream()
                        .anyMatch(e -> "CRITICAL".equals(e.get("severity")));

                if (hasCriticalErrors) {
                    validationStep.setStatus("FAILED");
                    fileJob.setStatus("VALIDATION_FAILED");
                    fileJob.setErrorMessage("Critical validation errors found");
                } else {
                    validationStep.setStatus("COMPLETED_WITH_WARNINGS");
                }
            }

        } catch (Exception e) {
            validationStep.setStatus("FAILED");
            validationStep.setDetails("{\"error\": \"" + e.getMessage() + "\"}");
        } finally {
            validationStep.setEndTime(LocalDateTime.now());
            fileJob.getProcessingSteps().add(validationStep);
        }

        return validationStep;
    }

    public ValidationRuleDto createRule(@Valid ValidationRuleDto ruleDto) {
        ValidationRule rule = new ValidationRule();
        rule.setRuleName(ruleDto.getRulename());
        rule.setRuleType(ruleDto.getRuleType());
        rule.setRuleConfig(ruleDto.getRuleConfig());
        rule.setSeverity(ruleDto.getSeverity());
        rule.setJobId(ruleDto.getJobId());
        rule.setCreatedAt(LocalDateTime.now());

        ValidationRule savedRule = validationRuleRepository.save(rule);
        return mapToDto(savedRule);
    }

    private ValidationRuleDto mapToDto(ValidationRule rule) {
        ValidationRuleDto dto = new ValidationRuleDto();
        dto.setId(rule.getId());
        dto.setJobId(rule.getJobId());
        dto.setRulename(rule.getRuleName());
        dto.setRuleType(rule.getRuleType());
        dto.setRuleConfig(rule.getRuleConfig());
        dto.setSeverity(rule.getSeverity());
        dto.setCreatedAt(rule.getCreatedAt());
        return dto;
    }

    public List<ValidationRuleDto> getRulesForJob(Long jobId) {
        List<ValidationRule> rules = validationRuleRepository.findByJobId(jobId);
        return rules.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ValidationRuleDto updateRule(Long ruleId, @Valid ValidationRuleDto ruleDto) {
        ValidationRule rule = validationRuleRepository.findById(ruleId)
                .orElseThrow(() -> new ResourceNotFoundException("ValidationRule", "id", ruleId));

        rule.setRuleName(ruleDto.getRulename());
        rule.setRuleType(ruleDto.getRuleType());
        rule.setRuleConfig(ruleDto.getRuleConfig());
        rule.setSeverity(ruleDto.getSeverity());

        ValidationRule updatedRule = validationRuleRepository.save(rule);
        return mapToDto(updatedRule);
    }

    public void deleteRule(Long ruleId) {
        if (!validationRuleRepository.existsById(ruleId)) {
            throw new ResourceNotFoundException("ValidationRule", "id", ruleId);
        }
        validationRuleRepository.deleteById(ruleId);
    }
}
