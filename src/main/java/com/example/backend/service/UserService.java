package com.example.backend.service;

import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.SignupRequest;
import com.example.backend.dto.UserResponse;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserResponse signup(SignupRequest request) {
        if (request.getName() == null || request.getEmail() == null || request.getPassword() == null || request.getRole() == null) {
            throw new IllegalArgumentException("Missing signup fields.");
        }
        
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already registered.");
        }
        
        User user = new User();
        user.setId(String.valueOf(System.currentTimeMillis()));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        
        userRepository.save(user);
        
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    public UserResponse login(LoginRequest request) {
        if (request.getEmail() == null || request.getPassword() == null || request.getRole() == null) {
            throw new IllegalArgumentException("Missing login fields.");
        }
        
        Optional<User> userOpt = userRepository.findByEmailAndPasswordAndRole(request.getEmail(), request.getPassword(), request.getRole());
        if (userOpt.isEmpty()) {
            throw new IllegalStateException("Invalid email, password or role.");
        }
        
        User user = userOpt.get();
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
