package com.example.nagoyameshi.service;

import com.example.nagoyameshi.entity.User;

public interface EmailService {
    /**
     * メール認証用のリンクを含むメールを送信する。
     *
     * @param user  送信先のユーザー
     * @param token 認証トークン
     */
    void sendVerificationEmail(User user, String token);
}
