package com.example.file_ingestion_system.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends BaseException {
    
    public AuthorizationException(String message) {
        super(message, HttpStatus.FORBIDDEN, "AUTHORIZATION_ERROR");
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause, HttpStatus.FORBIDDEN, "AUTHORIZATION_ERROR");
    }
}