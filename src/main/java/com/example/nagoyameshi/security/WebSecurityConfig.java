package com.example.nagoyameshi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

import lombok.RequiredArgsConstructor;

/**
 * Web アプリケーションのセキュリティ設定を行うクラスです。
 * 設定値を変更することで認証やアクセス権限の挙動を制御できます。
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    /**
     * ユーザー情報取得処理を提供するサービスクラスです。
     * Spring Security はこのサービスを用いてログインユーザーを検索します。
     */
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * CSRF保護の有効・無効を切り替えるフラグです。
     * application.properties の security.enable-csrf で設定できます。
     * true を設定すると CSRF 保護が有効になります。
     */
    @Value("${security.enable-csrf:true}")
    private boolean enableCsrf;

    /**
     * アプリ全体のセキュリティフィルタチェーンを構成します。
     *
     * @param http HttpSecurity の設定オブジェクト
     * @return セキュリティフィルタチェーン
     * @throws Exception 設定時に問題が発生した場合
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF を有効にするかどうかはプロパティで切り替え
        if (enableCsrf) {
            http.csrf(Customizer.withDefaults());
        } else {
            // 一時的に CSRF を無効化する場合はこちらが実行されます
            http.csrf(csrf -> csrf.disable());
        }

        return http
                // URL ごとのアクセス許可設定
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/css/**",
                                "/images/**",
                                "/js/**",
                                "/storage/**",
                                "/",
                                "/signup",
                                "/signup/**",
                                "/register"
                        ).permitAll()
                        .requestMatchers("/subscription/register", "/subscription/create")
                            .hasRole("FREE_MEMBER")
                        .requestMatchers(
                                "/subscription/edit",
                                "/subscription/update",
                                "/subscription/cancel",
                                "/subscription/delete")
                            .hasRole("PAID_MEMBER")
                        // 会社概要・利用規約ページは誰でも閲覧可能 (管理者を除く)
                        .requestMatchers("/company", "/terms")
                            .hasAnyRole("ANONYMOUS", "FREE_MEMBER", "PAID_MEMBER")
                        // 予約機能はログイン会員のみ
                        .requestMatchers("/reservations/**", "/restaurants/*/reservations/**")
                            .hasAnyRole("FREE_MEMBER", "PAID_MEMBER")
                        // レビュー機能はログイン会員のみ
                        .requestMatchers("/restaurants/*/reviews/**")
                            .hasAnyRole("FREE_MEMBER", "PAID_MEMBER")
                        // お気に入り機能はログイン会員のみ
                        .requestMatchers("/favorites/**", "/restaurants/*/favorites/**")
                            .hasAnyRole("FREE_MEMBER", "PAID_MEMBER")
                        // 会員向け店舗一覧は誰でも閲覧可能（未ログイン含む）
                        .requestMatchers("/restaurants/**")
                            .hasAnyRole("ANONYMOUS", "FREE_MEMBER", "PAID_MEMBER")
                        // 管理画面のURLは管理者ロールのみ許可
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                // ログイン時に利用する UserDetailsService を指定
                .userDetailsService(userDetailsService)
                // ログインフォームの設定
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/?loggedIn", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                // ログアウト時の設定
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/?loggedOut")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .build();
    }
}
