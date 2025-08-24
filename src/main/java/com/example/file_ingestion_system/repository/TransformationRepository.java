package com.example.file_ingestion_system.repository;

import com.example.file_ingestion_system.model.entity.Transformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransformationRepository extends JpaRepository<Transformation, Long> {
    List<Transformation> findByStepIdOrderByTransformOrder(Long stepId);
}
