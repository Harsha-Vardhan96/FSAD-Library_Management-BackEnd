package com.example.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP for Password Reset");
        message.setText("Hello,\n\nYour OTP for password reset is: " + otp + "\n\nThis OTP is valid for 15 minutes.\nIf you did not request a password reset, please ignore this email.\n\nThank you.");
        mailSender.send(message);
    }
}
