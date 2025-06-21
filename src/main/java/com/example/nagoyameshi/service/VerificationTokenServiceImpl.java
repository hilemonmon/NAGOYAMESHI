package com.example.nagoyameshi.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.entity.VerificationToken;
import com.example.nagoyameshi.repository.VerificationTokenRepository;

import lombok.RequiredArgsConstructor;

/**
 * VerificationTokenService の実装クラス。
 * データベースへの保存や検索を行うだけのシンプルなサービス。
 */
@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {
    /** VerificationToken エンティティを操作するリポジトリ */
    private final VerificationTokenRepository verificationTokenRepository;

    /** {@inheritDoc} */
    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken vToken = VerificationToken.builder()
                .user(user)
                .token(token)
                .build();
        // JPA を通じて保存
        verificationTokenRepository.save(vToken);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<VerificationToken> findVerificationTokenByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }
}
