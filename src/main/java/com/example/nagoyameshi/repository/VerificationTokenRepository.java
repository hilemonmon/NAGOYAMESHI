package com.example.nagoyameshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.example.nagoyameshi.entity.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {
    /**
     * 指定したトークンに一致する VerificationToken を取得する。
     *
     * @param token 検索するトークン
     * @return 一致する VerificationToken。存在しない場合は空
     */
    Optional<VerificationToken> findByToken(String token);
}
