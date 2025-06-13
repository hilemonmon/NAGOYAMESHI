package com.example.nagoyameshi.service;

import com.example.nagoyameshi.entity.User;

public interface EmailService {
    void sendVerificationEmail(User user);
}
