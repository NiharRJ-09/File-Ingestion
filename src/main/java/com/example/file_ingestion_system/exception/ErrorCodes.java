package com.example.file_ingestion_system.exception;

public final class ErrorCodes {
    
    // Authentication & Authorization
    public static final String AUTHENTICATION_ERROR = "AUTHENTICATION_ERROR";
    public static final String AUTHORIZATION_ERROR = "AUTHORIZATION_ERROR";
    public static final String ACCESS_DENIED = "ACCESS_DENIED";
    public static final String INVALID_TOKEN = "INVALID_TOKEN";
    public static final String TOKEN_EXPIRED = "TOKEN_EXPIRED";
    
    // Resource Management
    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    public static final String DUPLICATE_RESOURCE = "DUPLICATE_RESOURCE";
    
    // File Operations
    public static final String FILE_STORAGE_ERROR = "FILE_STORAGE_ERROR";
    public static final String FILE_PROCESSING_ERROR = "FILE_PROCESSING_ERROR";
    public static final String INVALID_FILE_TYPE = "INVALID_FILE_TYPE";
    public static final String FILE_SIZE_EXCEEDED = "FILE_SIZE_EXCEEDED";
    public static final String FILE_NOT_FOUND = "FILE_NOT_FOUND";
    
    // Validation
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String TRANSFORMATION_ERROR = "TRANSFORMATION_ERROR";
    
    // Job Processing
    public static final String JOB_PROCESSING_ERROR = "JOB_PROCESSING_ERROR";
    public static final String JOB_NOT_FOUND = "JOB_NOT_FOUND";
    public static final String JOB_ALREADY_PROCESSED = "JOB_ALREADY_PROCESSED";
    
    // Rate Limiting
    public static final String RATE_LIMIT_EXCEEDED = "RATE_LIMIT_EXCEEDED";
    
    // Generic
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    public static final String BAD_REQUEST = "BAD_REQUEST";
    public static final String ILLEGAL_ARGUMENT = "ILLEGAL_ARGUMENT";
    
    private ErrorCodes() {
        // Utility class
    }
}