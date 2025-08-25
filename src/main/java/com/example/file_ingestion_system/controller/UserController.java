package com.example.file_ingestion_system.controller;

import com.example.file_ingestion_system.model.dto.UserDto;
import com.example.file_ingestion_system.model.entity.User;
import com.example.file_ingestion_system.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "User management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get current user profile")
    @GetMapping("/profile")
    public UserDto getCurrentUser(@AuthenticationPrincipal User user) {
        return userService.getUserDto(user);
    }

    @Operation(summary = "Update current user profile")
    @PutMapping("/profile")
    public UserDto updateProfile(@AuthenticationPrincipal User user, @Valid @RequestBody UserDto userDto) {
        return userService.updateUser(user.getId(), userDto);
    }

    @Operation(summary = "Get all users (admin only)")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @Operation(summary = "Get user by ID (admin only)")
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @Operation(summary = "Update user (admin only)")
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto updateUser(@PathVariable Long userId, @Valid @RequestBody UserDto userDto) {
        return userService.updateUser(userId, userDto);
    }

    @Operation(summary = "Delete user (admin only)")
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}