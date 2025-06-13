package com.example.nagoyameshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.example.nagoyameshi.entity.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    // トークン文字列から認証情報を取得する
    Optional<VerificationToken> findByToken(String token);
}
