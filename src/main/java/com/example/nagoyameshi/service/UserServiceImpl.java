package com.example.nagoyameshi.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.dto.RegisterRequest;
import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.entity.VerificationToken;
import com.example.nagoyameshi.repository.RoleRepository;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.repository.VerificationTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public User register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        String encoded = passwordEncoder.encode(request.getPassword());
        // メール認証用のランダムなトークンを生成
        String token = UUID.randomUUID().toString();
        Role role = roleRepository.findByName("ROLE_FREE_MEMBER")
                .orElseThrow();
        // ユーザー情報を保存（この時点ではまだ有効化されていない）
        User user = User.builder()
                .name("")
                .email(request.getEmail())
                .password(encoded)
                .role(role)
                .enabled(false)
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .build();
        userRepository.save(user);

        // 発行したトークンをverification_tokensテーブルに保存
        VerificationToken vToken = VerificationToken.builder()
                .user(user)
                .token(token)
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .build();
        verificationTokenRepository.save(vToken);

        // 確認メールを送信
        emailService.sendVerificationEmail(user, token);
        return user;
    }

    @Override
    public boolean verify(String token) {
        Optional<VerificationToken> opt = verificationTokenRepository.findByToken(token);
        if (opt.isEmpty()) {
            return false;
        }
        User user = opt.get().getUser();
        user.setEnabled(true);
        userRepository.save(user);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().getName())
                .disabled(!user.isEnabled())
                .build();
    }
}
