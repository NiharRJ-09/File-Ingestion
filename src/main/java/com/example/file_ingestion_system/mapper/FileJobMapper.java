package com.example.file_ingestion_system.mapper;

import com.example.file_ingestion_system.model.dto.FileJobDto;
import com.example.file_ingestion_system.model.entity.FileJob;
import org.springframework.stereotype.Component;

@Component
public class FileJobMapper {
    
    public FileJobDto toDto(FileJob fileJob) {
        if (fileJob == null) {
            return null;
        }
        
        FileJobDto dto = new FileJobDto();
        dto.setId(fileJob.getId());
        dto.setUsername(fileJob.getUser().getUsername());
        dto.setFilename(fileJob.getFilename());
        dto.setOriginalFilename(fileJob.getOriginalFilename());
        dto.setFileType(fileJob.getFileType());
        dto.setUploadDate(fileJob.getUploadDate());
        dto.setStatus(fileJob.getStatus());
        dto.setErrorMessage(fileJob.getErrorMessage());
        dto.setResultLocation(fileJob.getResultLocation());
        
        return dto;
    }
}