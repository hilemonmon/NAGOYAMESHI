package com.example.nagoyameshi.service;

import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.User;

@Service
public class ConsoleEmailService implements EmailService {
    @Override
    public void sendVerificationEmail(User user, String token) {
        // 実際にはメール送信を行うが、サンプルではコンソールに出力する
        String url = "https://ドメイン名/signup/verify?token=" + token;
        System.out.println("Send verification email to " + user.getEmail()
                + " link=" + url);
    }
}
