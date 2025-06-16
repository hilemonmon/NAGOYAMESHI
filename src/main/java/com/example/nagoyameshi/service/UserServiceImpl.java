package com.example.nagoyameshi.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.Optional;

// 認証用の UserDetailsService 実装は security パッケージへ分離したため
// ここではインターフェースを実装しない
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.dto.RegisterRequest;
import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.entity.VerificationToken;
import com.example.nagoyameshi.form.SignupForm;
import com.example.nagoyameshi.repository.RoleRepository;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.repository.VerificationTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public User register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        String encoded = passwordEncoder.encode(request.getPassword());
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

        // メールはイベントリスナーで送信されるため、ここでは登録のみ行う
        return user;
    }

    @Override
    public void createVerificationToken(User user, String token) {
        // 現在時刻を Timestamp で取得
        Timestamp now = new Timestamp(System.currentTimeMillis());

        VerificationToken vToken = VerificationToken.builder()
                .user(user)
                .token(token)
                .createdAt(now)
                .updatedAt(now)
                .build();
        verificationTokenRepository.save(vToken);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void createUser(SignupForm form) {
        Role role = roleRepository.findByName("ROLE_FREE_MEMBER").orElseThrow();

        User user = User.builder()
                .name(form.getName())
                .furigana(form.getFurigana())
                .postalCode(form.getPostalCode())
                .address(form.getAddress())
                .phoneNumber(form.getPhoneNumber())
                .birthday(form.getBirthday() == null || form.getBirthday().isEmpty()
                        ? null
                        : LocalDate.parse(form.getBirthday()))
                .occupation(form.getOccupation() == null || form.getOccupation().isEmpty()
                        ? null
                        : form.getOccupation())
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .role(role)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmailRegistered(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSamePassword(String password, String passwordConfirmation) {
        if (password == null) {
            return passwordConfirmation == null;
        }
        return password.equals(passwordConfirmation);
    }
}
