package com.example.file_ingestion_system.repository;

import com.example.file_ingestion_system.model.entity.ValidationRule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ValidationRuleRepository extends JpaRepository<ValidationRule, Long> {
    List<ValidationRule> findByJobId(Long jobId);
    List<ValidationRule> findByRuleType(String ruleType);
}
