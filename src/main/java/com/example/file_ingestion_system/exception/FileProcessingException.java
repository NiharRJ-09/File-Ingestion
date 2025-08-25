package com.example.file_ingestion_system.exception;

import org.springframework.http.HttpStatus;

public class FileProcessingException extends BaseException {
    
    public FileProcessingException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "FILE_PROCESSING_ERROR");
    }

    public FileProcessingException(String message, Throwable cause) {
        super(message, cause, HttpStatus.BAD_REQUEST, "FILE_PROCESSING_ERROR");
    }
}