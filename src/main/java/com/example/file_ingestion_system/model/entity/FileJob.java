package com.example.file_ingestion_system.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "file_jobs")
@Data
public class FileJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String filename;

    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "upload_date", nullable = false)
    private LocalDateTime uploadDate;

    @Column(nullable = false)
    private String status;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "result_location")
    private String resultLocation;

    @OneToMany(mappedBy = "fileJob", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProcessingStep> processingSteps = new ArrayList<>();
}
