package com.example.file_ingestion_system.service.validation.rules;

import com.example.file_ingestion_system.service.validation.ValidationRule;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotNullValidationRule implements ValidationRule {
    @Override
    public String getRuleName() {
        return "not_null";
    }

    @Override
    public String getRuleType() {
        return "DATA_QUALITY";
    }

    @Override
    public boolean validate(Map<String, Object> row, Map<String, Object> ruleConfig) {
        String fieldName = (String) ruleConfig.get("fieldName");
        Object value = row.get(fieldName);
        return value != null && !value.toString().trim().isEmpty();
    }

    @Override
    public String getErrorMessage(Map<String, Object> ruleConfig) {
        String fieldName = (String) ruleConfig.get("fieldName");
        return "Field '" + fieldName + "' cannot be null or empty";
    }
}
