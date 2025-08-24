package com.example.file_ingestion_system.service.validation;

import com.example.file_ingestion_system.model.entity.ValidationRule;
import com.example.file_ingestion_system.repository.ValidationRuleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ValidationRuleConfigLoader {
    private final ValidationRuleRepository validationRuleRepository;
    private final ObjectMapper objectMapper;

    public ValidationRuleConfigLoader(
            ValidationRuleRepository validationRuleRepository,
            ObjectMapper objectMapper) {
        this.validationRuleRepository = validationRuleRepository;
        this.objectMapper = objectMapper;
    }

    public List<ValidationRule> loadRulesFromFile(Path configFile) throws IOException {
        String content = Files.readString(configFile);
        Map<String, Object> config = objectMapper.readValue(content, Map.class);

        List<ValidationRule> rules = new ArrayList<>();

        List<Map<String, Object>> rulesConfig = (List<Map<String, Object>>) config.get("rules");
        for (Map<String, Object> ruleConfig : rulesConfig) {
            ValidationRule rule = new ValidationRule();
            rule.setRuleName((String) ruleConfig.get("name"));
            rule.setRuleType((String) ruleConfig.get("type"));
            rule.setRuleConfig(objectMapper.writeValueAsString(ruleConfig.get("config")));
            rule.setSeverity((String) ruleConfig.get("severity"));
            rules.add(rule);
        }

        return rules;
    }

    public void saveRules(List<ValidationRule> rules, Long jobId) {
        rules.forEach(rule -> {
            rule.setJobId(jobId);
            validationRuleRepository.save(rule);
        });
    }
}
