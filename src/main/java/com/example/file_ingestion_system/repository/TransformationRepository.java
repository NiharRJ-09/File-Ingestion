package com.example.file_ingestion_system.repository;

import com.example.file_ingestion_system.model.entity.Transformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransformationRepository extends JpaRepository<Transformation, Long> {
    List<Transformation> findByStepIdOrderByTransformOrder(Long stepId);

    List<Transformation> findByProcessingStepIdOrderByTransformOrder(Long stepId);
    List<Transformation> findByProcessingStepId(Long stepId);

    void deleteByProcessingStepId(Long stepId);

}
