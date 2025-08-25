package com.example.file_ingestion_system.mapper;

import com.example.file_ingestion_system.model.dto.UserDto;
import com.example.file_ingestion_system.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole().name());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setEnabled(user.isEnabled());

        return dto;
    }
}