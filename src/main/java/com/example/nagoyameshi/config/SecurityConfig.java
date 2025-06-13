package com.example.nagoyameshi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.example.nagoyameshi.service.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    /*
     * PasswordEncoder は別クラス(PasswordConfig)で定義し、
     * UserServiceImpl が依存する Bean を分離することで
     * 循環依存を防止している。
     */

    /**
     * 認証時にユーザー情報を取得するサービス実装です。
     * UserServiceImpl は UserDetailsService を実装しているため、
     * SecurityFilterChain の設定にそのまま利用できます。
     */
    private final UserServiceImpl userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/css/**",
                    "/images/**",
                    "/js/**",
                    "/storage/**",
                    "/",
                    "/signup/**",
                    "/register",
                    "/verify"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .userDetailsService(userService)
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/?loggedIn", true)
                .failureUrl("/login?error")
                .permitAll()
            )
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
