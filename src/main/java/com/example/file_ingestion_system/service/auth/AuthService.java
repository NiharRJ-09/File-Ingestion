package com.example.file_ingestion_system.service.auth;

import com.example.file_ingestion_system.dto.*;
import com.example.file_ingestion_system.mapper.UserMapper;
import com.example.file_ingestion_system.model.dto.LoginRequest;
import com.example.file_ingestion_system.model.dto.LoginResponse;
import com.example.file_ingestion_system.model.dto.RegisterRequest;
import com.example.file_ingestion_system.model.dto.UserDto;
import com.example.file_ingestion_system.model.entity.User;
import com.example.file_ingestion_system.repository.UserRepository;
//import com.example.file_ingestion_system.security.JwtTokenProvider;
import com.example.file_ingestion_system.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils tokenProvider;
    private final UserMapper userMapper;

    public AuthService(AuthenticationManager authenticationManager, 
                      UserRepository userRepository,
                      PasswordEncoder passwordEncoder,
                      JwtUtils tokenProvider,
                      UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.userMapper = userMapper;
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String accessToken = tokenProvider.generateToken(user);
        String refreshToken = tokenProvider.generateRefreshToken(user);

        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setUser(userMapper.toDto(user));
        
        return response;
    }

    public UserDto register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    public LoginResponse refreshToken(String refreshToken) {
        if (tokenProvider.validateToken(refreshToken)) {
            String username = tokenProvider.getUsernameFromToken(refreshToken);
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

            String newAccessToken = tokenProvider.generateToken(username, user.getRole().name());
            
            LoginResponse response = new LoginResponse();
            response.setAccessToken(newAccessToken);
            response.setRefreshToken(refreshToken);
            response.setUser(userMapper.toDto(user));
            
            return response;
        }
        throw new RuntimeException("Invalid refresh token");
    }
}