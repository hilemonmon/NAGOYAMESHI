package com.example.nagoyameshi.service;

import com.example.nagoyameshi.dto.RegisterRequest;
import com.example.nagoyameshi.entity.User;

public interface UserService {
    User register(RegisterRequest request);
    boolean verify(String token);

    /**
     * 認証トークンを生成して保存する。
     *
     * @param user  対象のユーザー
     * @param token 保存するトークン
     */
    void createVerificationToken(User user, String token);
}
