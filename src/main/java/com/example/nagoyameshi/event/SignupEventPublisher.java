package com.example.nagoyameshi.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.nagoyameshi.entity.User;

import lombok.RequiredArgsConstructor;

/**
 * サインアップイベントを発行するためのクラス。
 * ApplicationEventPublisher を利用してイベントを通知する。
 */
@Component
@RequiredArgsConstructor
public class SignupEventPublisher {
    private final ApplicationEventPublisher publisher;

    /**
     * ユーザー登録完了時にイベントを発行する。
     *
     * @param user       登録したユーザー
     * @param requestUrl リクエストURL (例: https://ドメイン名/signup)
     */
    public void publish(User user, String requestUrl) {
        SignupEvent event = new SignupEvent(this, user, requestUrl);
        publisher.publishEvent(event);
    }
}
