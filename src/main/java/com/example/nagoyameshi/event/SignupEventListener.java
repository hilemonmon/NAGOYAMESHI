package com.example.nagoyameshi.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.nagoyameshi.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * SignupEvent を受け取り、認証メールを送信するリスナー。
 */
@Component
@RequiredArgsConstructor
public class SignupEventListener {
    private final JavaMailSender mailSender;
    private final UserService userService;

    /**
     * サインアップイベントを受信して処理を行う。
     *
     * @param event 発生したSignupEvent
     */
    @EventListener
    public void onSignupEvent(SignupEvent event) {
        // ランダムなトークンを生成
        String token = UUID.randomUUID().toString();

        // トークン情報を保存
        userService.createVerificationToken(event.getUser(), token);

        // 認証用URLを作成
        String verifyUrl = event.getRequestUrl() + "/verify?token=" + token;

        // メール内容を組み立てる
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getUser().getEmail());
        message.setSubject("メールアドレス確認");
        message.setText("以下のリンクからメールアドレスを確認してください: " + verifyUrl);

        // メールを送信
        mailSender.send(message);
    }
}
