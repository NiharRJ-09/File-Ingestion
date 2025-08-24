package com.example.file_ingestion_system.service.validation;
import java.util.Map;
public interface ValidationRule {
    String getRuleName();
    String getRuleType();
    boolean validate(Map<String, Object> row, Map<String, Object> ruleConfig);
    String getErrorMessage(Map<String, Object> ruleConfig);
}
