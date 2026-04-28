package com.example.backend.service;

import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.SignupRequest;
import com.example.backend.dto.UserResponse;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

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

    public void generateOtp(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User with given email not found.");
        }
        
        User user = userOpt.get();
        
        // Generate 6-digit OTP
        Random random = new Random();
        int otpNum = 100000 + random.nextInt(900000);
        String otp = String.valueOf(otpNum);
        
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(15));
        
        userRepository.save(user);
        
        // Send email
        emailService.sendOtpEmail(user.getEmail(), otp);
    }

    public void resetPassword(String email, String otp, String newPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User with given email not found.");
        }
        
        User user = userOpt.get();
        
        if (user.getOtp() == null || !user.getOtp().equals(otp)) {
            throw new IllegalArgumentException("Invalid OTP.");
        }
        
        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("OTP has expired.");
        }
        
        user.setPassword(newPassword);
        user.setOtp(null);
        user.setOtpExpiry(null);
        
        userRepository.save(user);
    }
}
