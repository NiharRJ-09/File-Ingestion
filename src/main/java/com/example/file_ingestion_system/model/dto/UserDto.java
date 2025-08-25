package com.example.file_ingestion_system.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private boolean enabled;
}
