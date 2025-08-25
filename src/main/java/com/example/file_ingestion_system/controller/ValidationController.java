package com.example.file_ingestion_system.controller;

import com.example.file_ingestion_system.model.dto.ValidationRuleDto;
import com.example.file_ingestion_system.service.validation.ValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/validation")
@Tag(name = "Validation", description = "Data validation rule management")
@SecurityRequirement(name = "bearerAuth")
public class ValidationController {

    private final ValidationService validationService;

    public ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @Operation(summary = "Create validation rule")
    @PostMapping("/rules")
    @ResponseStatus(HttpStatus.CREATED)
    public ValidationRuleDto createRule(@Valid @RequestBody ValidationRuleDto ruleDto) {
        return validationService.createRule(ruleDto);
    }

    @Operation(summary = "Get validation rules for job")
    @GetMapping("/rules/job/{jobId}")
    public List<ValidationRuleDto> getRulesForJob(@PathVariable Long jobId) {
        return validationService.getRulesForJob(jobId);
    }

    @Operation(summary = "Update validation rule")
    @PutMapping("/rules/{ruleId}")
    public ValidationRuleDto updateRule(@PathVariable Long ruleId, @Valid @RequestBody ValidationRuleDto ruleDto) {
        return validationService.updateRule(ruleId, ruleDto);
    }

    @Operation(summary = "Delete validation rule")
    @DeleteMapping("/rules/{ruleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRule(@PathVariable Long ruleId) {
        validationService.deleteRule(ruleId);
    }
}