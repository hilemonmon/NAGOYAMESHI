package com.example.nagoyameshi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.nagoyameshi.dto.RegisterRequest;
import com.example.nagoyameshi.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Validated @RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verify")
    public ResponseEntity<Void> verify(@RequestParam("token") String token) {
        boolean result = userService.verify(token);
        return result ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
