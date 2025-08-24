package com.example.file_ingestion_system.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "processing_steps")
@Data
public class ProcessingStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private FileJob fileJob;

    @Column(name = "step_type", nullable = false)
    private String stepType;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String status;

    @Column(columnDefinition = "jsonb")
    private String details;

    @OneToMany(mappedBy = "processingStep", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transformation> transformations = new ArrayList<>();
}