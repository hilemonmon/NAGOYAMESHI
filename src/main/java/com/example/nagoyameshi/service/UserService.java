package com.example.nagoyameshi.service;

import com.example.nagoyameshi.dto.RegisterRequest;
import com.example.nagoyameshi.entity.User;

public interface UserService {
    User register(RegisterRequest request);
    boolean verify(String token);
}
