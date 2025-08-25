package com.example.file_ingestion_system.exception;

import org.springframework.http.HttpStatus;

public class TransformationException extends BaseException {
    
    public TransformationException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "TRANSFORMATION_ERROR");
    }

    public TransformationException(String message, Throwable cause) {
        super(message, cause, HttpStatus.BAD_REQUEST, "TRANSFORMATION_ERROR");
    }
}