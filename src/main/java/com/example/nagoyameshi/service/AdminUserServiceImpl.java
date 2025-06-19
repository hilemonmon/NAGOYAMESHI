package com.example.nagoyameshi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * {@link AdminUserService} の実装クラス。
 * データベースからユーザー情報を取得する。
 */
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    /** データベースアクセス用のリポジトリ */
    private final UserRepository userRepository;

    /** {@inheritDoc} */
    @Override
    public Page<User> searchUsers(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return userRepository.findAll(pageable);
        }
        return userRepository
                .findByNameContainingIgnoreCaseOrFuriganaContainingIgnoreCase(
                        keyword,
                        keyword,
                        pageable);
    }

    /** {@inheritDoc} */
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
