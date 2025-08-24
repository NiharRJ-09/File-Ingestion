package com.example.file_ingestion_system.repository;

import com.example.file_ingestion_system.model.entity.FileJob;
import com.example.file_ingestion_system.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FileJobRepository extends JpaRepository<FileJob, Long> {
    List<FileJob> findByUserId(Long userId);
    List<FileJob> findByStatus(String status);
    Page<FileJob> findByUserId(Long userId, Pageable pageable);
    List<FileJob> findByUploadDateBetween(LocalDateTime start, LocalDateTime end);

    Page<FileJob> findByUser(User user, Pageable pageable);

    Page<FileJob> findByStatus(String status, Pageable pageable);
}
