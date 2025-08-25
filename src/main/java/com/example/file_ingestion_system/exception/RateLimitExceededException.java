package com.example.file_ingestion_system.exception;

import org.springframework.http.HttpStatus;

public class RateLimitExceededException extends BaseException {
    
    public RateLimitExceededException(String message) {
        super(message, HttpStatus.TOO_MANY_REQUESTS, "RATE_LIMIT_EXCEEDED");
    }

    public RateLimitExceededException(String operation, int limit) {
        super(String.format("Rate limit exceeded for %s. Limit: %d requests", operation, limit),
              HttpStatus.TOO_MANY_REQUESTS, "RATE_LIMIT_EXCEEDED");
    }
}