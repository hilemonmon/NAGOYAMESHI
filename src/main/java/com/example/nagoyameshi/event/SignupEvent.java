package com.example.nagoyameshi.event;

import org.springframework.context.ApplicationEvent;

import com.example.nagoyameshi.entity.User;

import lombok.Getter;

/**
 * ユーザー登録が行われたことを知らせるイベントクラス。
 * Listenerにユーザー情報とリクエストURLを渡す。
 */
@Getter
public class SignupEvent extends ApplicationEvent {
    /** 登録したユーザー */
    private final User user;
    /** アプリケーションのベースURL (例: https://ドメイン名) */
    private final String requestUrl;

    /**
     * @param source     イベントの発生元 (Publisher のインスタンス)
     * @param user       登録ユーザー
     * @param requestUrl アプリケーションのベースURL
     */
    public SignupEvent(Object source, User user, String requestUrl) {
        super(source);
        this.user = user;
        this.requestUrl = requestUrl;
    }
}
