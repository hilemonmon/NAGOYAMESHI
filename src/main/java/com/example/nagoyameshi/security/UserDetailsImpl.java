package com.example.nagoyameshi.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.nagoyameshi.entity.User;

/**
 * Spring Security が利用するユーザー情報の実装クラスです。
 * アプリケーションの User エンティティをラップし、
 * 権限情報とともに提供します。
 */
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    /** ユーザー情報を保持する */
    private final User user;

    /** ロールなどの権限情報 */
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * コンストラクタでユーザー情報と権限を受け取り保持します。
     *
     * @param user        アプリケーションのユーザーエンティティ
     * @param authorities 権限のコレクション
     */
    public UserDetailsImpl(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    /**
     * ログイン時に利用するメールアドレスを返します。
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * ハッシュ化済みのパスワードを返します。
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * ユーザーの権限情報を返します。
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * 本アプリではアカウント有効期限を使用しないため常に true を返します。
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 本アプリではアカウントロックを使用しないため常に true を返します。
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 本アプリでは資格情報の有効期限を使用しないため常に true を返します。
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * ユーザーが有効であるかどうかを返します。
     */
    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    /**
     * ログイン中のユーザーエンティティを取得するためのヘルパーメソッドです。
     *
     * @return 内部で保持している User オブジェクト
     */
    public User getUser() {
        return user;
    }
}
