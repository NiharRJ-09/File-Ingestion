package com.example.file_ingestion_system.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "validation_rules")
@Data
public class ValidationRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private FileJob fileJob;

    @Column(name = "rule_name", nullable = false, length = 100)
    private String ruleName;

    @Column(name = "rule_type", nullable = false, length = 50)
    private String ruleType;

    @Column(name = "rule_config", nullable = false, columnDefinition = "jsonb")
    private String ruleConfig;

    @Column(nullable = false, length = 20)
    private String severity;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Object getErrorMessage(Map<String, Object> ruleConfig) {
        return null;
    }

    public void setJobId(Long jobId) {
        this.fileJob = new FileJob();
        this.fileJob.setId(jobId);
    }

    public boolean validate(Map<String, Object> row, Map<String, Object> ruleConfig) {
        return false;
    }

    public Long getJobId() {
        return fileJob.getId();
    }
}
