package com.example.file_ingestion_system.service.validation;

import com.example.file_ingestion_system.model.entity.ValidationRule;
import com.example.file_ingestion_system.repository.ValidationRuleRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidationRuleService {

    private final ValidationRuleRepository validationRuleRepository;

    public ValidationRuleService(ValidationRuleRepository validationRuleRepository) {
        this.validationRuleRepository = validationRuleRepository;
    }

    @Cacheable(value = "validationRules", key = "#jobId")
    public List<ValidationRule> getRulesForJob(Long jobId) {
        return validationRuleRepository.findByJobId(jobId);
    }
}
