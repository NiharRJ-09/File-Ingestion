package com.example.file_ingestion_system.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "transformations")
@Data
public class Transformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "step_id")
    private ProcessingStep processingStep;

    @Column(name = "transform_type", nullable = false, length = 50)
    private String transformType;

    @Column(name = "source_field", length = 100)
    private String sourceField;

    @Column(name = "target_field", length = 100)
    private String targetField;

    @Column(name = "transform_config", columnDefinition = "jsonb")
    private String transformConfig;

    @Column(name = "transform_order", nullable = false)
    private Integer transformOrder;
}