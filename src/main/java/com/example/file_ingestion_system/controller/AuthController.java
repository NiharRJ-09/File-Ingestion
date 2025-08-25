package com.example.file_ingestion_system.controller;

import com.example.file_ingestion_system.model.dto.LoginRequest;
import com.example.file_ingestion_system.model.dto.LoginResponse;
import com.example.file_ingestion_system.model.dto.RegisterRequest;
import com.example.file_ingestion_system.model.dto.UserDto;
import com.example.file_ingestion_system.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication and authorization endpoints")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "User login")
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @Operation(summary = "User registration")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @Operation(summary = "Refresh JWT token")
    @PostMapping("/refresh")
    public LoginResponse refresh(@RequestParam String refreshToken) {
        return authService.refreshToken(refreshToken);
    }

}
