package com.example.file_ingestion_system.repository;

import com.example.file_ingestion_system.model.entity.ProcessingStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessingStepRepository extends JpaRepository<ProcessingStep, Long> {
    List<ProcessingStep> findByFileJobId(Long jobId);

    List<ProcessingStep> findByFileJobIdOrderByStartTime(Long jobId);

    List<ProcessingStep> findByStatus(String status);
}