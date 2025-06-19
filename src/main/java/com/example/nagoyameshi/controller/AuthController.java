package com.example.nagoyameshi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import com.example.nagoyameshi.dto.RegisterRequest;
import com.example.nagoyameshi.event.SignupEventPublisher;
import com.example.nagoyameshi.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final SignupEventPublisher signupEventPublisher;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Validated @RequestBody RegisterRequest request,
                                         HttpServletRequest httpRequest) {
        // ユーザー登録処理を実行
        com.example.nagoyameshi.entity.User user = userService.register(request);
        // リクエストURLからドメイン部分のみを取得する
        // 例: http://localhost:8080/register -> http://localhost:8080
        String baseUrl = httpRequest.getRequestURL().toString().replace("/register", "");
        signupEventPublisher.publish(user, baseUrl);
        return ResponseEntity.ok().build();
    }

}
