package com.example.file_ingestion_system.service.job;

import com.example.file_ingestion_system.model.entity.FileJob;
import com.example.file_ingestion_system.model.entity.User;
import com.example.file_ingestion_system.repository.FileJobRepository;
import com.example.file_ingestion_system.repository.ProcessingStepRepository;
import com.example.file_ingestion_system.service.file.FileStorageService;
import com.example.file_ingestion_system.service.file.processor.FileProcessor;
import com.example.file_ingestion_system.service.file.processor.FileProcessorFactory;
import com.example.file_ingestion_system.service.notification.EmailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class JobManagementService {

    private final FileJobRepository fileJobRepository;
    private final ProcessingStepRepository processingStepRepository;
    private final FileStorageService fileStorageService;
    private final FileProcessorFactory fileProcessorFactory;
    private final EmailService emailService;

    public JobManagementService(
            FileJobRepository fileJobRepository,
            ProcessingStepRepository processingStepRepository,
            FileStorageService fileStorageService,
            FileProcessorFactory fileProcessorFactory,
            EmailService emailService) {
        this.fileJobRepository = fileJobRepository;
        this.processingStepRepository = processingStepRepository;
        this.fileStorageService = fileStorageService;
        this.fileProcessorFactory = fileProcessorFactory;
        this.emailService = emailService;
    }

    @Transactional
    public FileJob createFileJob(MultipartFile file, String fileType, User user) throws Exception {
        // Store the file
        String storedFileName = fileStorageService.storeFile(file, fileType);

        // Create job record
        FileJob fileJob = new FileJob();
        fileJob.setFilename(storedFileName);
        fileJob.setOriginalFilename(file.getOriginalFilename());
        fileJob.setFileType(fileType);
        fileJob.setUploadDate(LocalDateTime.now());
        fileJob.setStatus("UPLOADED");
        fileJob.setUser(user);

        // Save job
        fileJob = fileJobRepository.save(fileJob);

        // Process the file asynchronously
        processFileAsync(fileJob.getId());

        return fileJob;
    }

    @Async
    protected void processFileAsync(Long jobId) {
        FileJob fileJob = fileJobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        try {
            fileJob.setStatus("PROCESSING");
            fileJobRepository.save(fileJob);

            FileProcessor processor = fileProcessorFactory.getProcessor(fileJob.getFileType());
            processor.processFile(fileJob);

            fileJob.setStatus("COMPLETED");
            fileJobRepository.save(fileJob);

            // Send notification
            emailService.sendJobCompletedNotification(fileJob);

        } catch (Exception e) {
            fileJob.setStatus("FAILED");
            fileJob.setErrorMessage(e.getMessage());
            fileJobRepository.save(fileJob);

            // Send failure notification
            emailService.sendJobFailedNotification(fileJob);
        }
    }

    public FileJob getJobById(Long jobId) {
        return fileJobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    public Page<FileJob> getUserJobs(User user, Pageable pageable) {
        return fileJobRepository.findByUser(user, pageable);
    }

    public Page<FileJob> getAllJobs(Pageable pageable) {
        return fileJobRepository.findAll(pageable);
    }

    public Page<FileJob> getJobsByStatus(String status, Pageable pageable) {
        return fileJobRepository.findByStatus(status, pageable);
    }
}
