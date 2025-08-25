package com.example.file_ingestion_system.exception;

import org.springframework.http.HttpStatus;

public class InvalidFileTypeException extends BaseException {
    
    public InvalidFileTypeException(String fileType) {
        super(String.format("Unsupported file type: %s", fileType),
              HttpStatus.BAD_REQUEST, "INVALID_FILE_TYPE");
    }

    public InvalidFileTypeException(String message, String fileType) {
        super(String.format("%s. File type: %s", message, fileType),
              HttpStatus.BAD_REQUEST, "INVALID_FILE_TYPE");
    }
}