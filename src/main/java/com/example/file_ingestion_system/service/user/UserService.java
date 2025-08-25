package com.example.file_ingestion_system.service.user;

import com.example.file_ingestion_system.model.dto.UserDto;
import com.example.file_ingestion_system.mapper.UserMapper;
import com.example.file_ingestion_system.model.entity.User;
import com.example.file_ingestion_system.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public UserDto getUserDto(User user) {
        return userMapper.toDto(user);
    }

    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(user);
    }

    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}