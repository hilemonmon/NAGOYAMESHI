package com.example.nagoyameshi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import com.example.nagoyameshi.dto.RegisterRequest;
import com.example.nagoyameshi.event.SignupEventPublisher;
import com.example.nagoyameshi.service.UserService;
import com.example.nagoyameshi.service.VerificationTokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
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

    @GetMapping("/verify")
    public ResponseEntity<Void> verify(@RequestParam("token") String token) {
        // まずトークンの存在を確認
        var vToken = verificationTokenService.findVerificationTokenByToken(token);
        if (vToken.isEmpty()) {
            // トークンが存在しない場合は 400 を返却
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // 実際の認証処理は UserService に委譲
        boolean result = userService.verify(token);
        return result ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
