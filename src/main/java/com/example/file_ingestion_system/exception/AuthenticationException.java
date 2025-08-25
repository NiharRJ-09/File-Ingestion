package com.example.file_ingestion_system.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends BaseException {
    
    public AuthenticationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "AUTHENTICATION_ERROR");
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause, HttpStatus.UNAUTHORIZED, "AUTHENTICATION_ERROR");
    }
}