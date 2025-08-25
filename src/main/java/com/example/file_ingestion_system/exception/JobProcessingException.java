package com.example.file_ingestion_system.exception;

import org.springframework.http.HttpStatus;

public class JobProcessingException extends BaseException {
    
    public JobProcessingException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, "JOB_PROCESSING_ERROR");
    }

    public JobProcessingException(String message, Throwable cause) {
        super(message, cause, HttpStatus.INTERNAL_SERVER_ERROR, "JOB_PROCESSING_ERROR");
    }
}