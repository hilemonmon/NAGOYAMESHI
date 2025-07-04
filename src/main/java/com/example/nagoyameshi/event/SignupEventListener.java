package com.example.nagoyameshi.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.example.nagoyameshi.service.VerificationTokenService;

import lombok.RequiredArgsConstructor;

/**
 * SignupEvent を受け取り、認証メールを送信するリスナー。
 */
@Component
@RequiredArgsConstructor
public class SignupEventListener {
    private final JavaMailSender mailSender;
    private final VerificationTokenService verificationTokenService;
    // application.properties の spring.mail.from プロパティを注入
    @Value("${spring.mail.from}")
    private String fromAddress;

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
        verificationTokenService.createVerificationToken(event.getUser(), token);

        // 認証用URLを作成
        // ベースURL + "/signup/verify?token=..." という形式になる
        String verifyUrl = event.getRequestUrl() + "/signup/verify?token=" + token;

        // メール内容を組み立てる
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(event.getUser().getEmail());
        message.setSubject("メールアドレス確認");
        message.setText("以下のリンクからメールアドレスを確認してください: " + verifyUrl);

        // メールを送信
        mailSender.send(message);
    }
}
