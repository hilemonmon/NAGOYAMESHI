package com.example.nagoyameshi.service;

import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.User;

@Service
public class ConsoleEmailService implements EmailService {
    @Override
    public void sendVerificationEmail(User user) {
        System.out.println("Send verification email to " + user.getEmail()
                + " token=" + user.getVerificationCode());
    }
}
