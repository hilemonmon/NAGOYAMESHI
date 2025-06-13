package com.example.nagoyameshi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * パスワードのハッシュ化アルゴリズムを提供する設定クラスです。
 * Spring Security 以外のサービスからも再利用できるよう、
 * SecurityConfig とは分離して定義します。
 */
@Configuration
public class PasswordConfig {
    /**
     * BCrypt を用いたパスワードエンコーダーの Bean を生成します。
     * これを用いることで平文のパスワードを安全にハッシュ化できます。
     *
     * @return PasswordEncoder の実装クラス
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
