package com.example.nagoyameshi.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// 認証用の UserDetailsService 実装は security パッケージへ分離したため
// ここではインターフェースを実装しない
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.dto.RegisterRequest;
import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.entity.VerificationToken;
import com.example.nagoyameshi.form.SignupForm;
import com.example.nagoyameshi.form.UserEditForm;
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
                .build();
        userRepository.save(user);

        // メールはイベントリスナーで送信されるため、ここでは登録のみ行う
        return user;
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken vToken = VerificationToken.builder()
                .user(user)
                .token(token)
                .build();
        verificationTokenRepository.save(vToken);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public User createUser(SignupForm form) {
        Role role = roleRepository.findByName("ROLE_FREE_MEMBER").orElseThrow();

        User user = User.builder()
                .name(form.getName())
                .furigana(form.getFurigana())
                .postalCode(form.getPostalCode())
                .address(form.getAddress())
                .phoneNumber(form.getPhoneNumber())
                // 誕生日は画面上で8桁の数字(yyyyMMdd)で入力されるため、
                // BASIC_ISO_DATE で LocalDate へ変換する
                .birthday(form.getBirthday() == null || form.getBirthday().isEmpty()
                        ? null
                        : LocalDate.parse(form.getBirthday(), DateTimeFormatter.BASIC_ISO_DATE))
                .occupation(form.getOccupation() == null || form.getOccupation().isEmpty()
                        ? null
                        : form.getOccupation())
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .role(role)
                // メール認証前のため、アカウントを無効状態で作成
                .enabled(false)
                .build();

        // ユーザーを保存して生成されたエンティティを返す
        return userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enableUser(User user) {
        user.setEnabled(true);
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

    /** {@inheritDoc} */
    @Override
    public User updateUser(UserEditForm form, User user) {
        user.setName(form.getName());
        user.setFurigana(form.getFurigana());
        user.setPostalCode(form.getPostalCode());
        user.setAddress(form.getAddress());
        user.setPhoneNumber(form.getPhoneNumber());
        // 空文字の場合は null を設定する
        if (form.getBirthday() == null || form.getBirthday().isEmpty()) {
            // 入力が空の場合は誕生日を登録しない
            user.setBirthday(null);
        } else {
            // 入力値は "yyyyMMdd" の8桁数字なので、BASIC_ISO_DATE で解析する
            user.setBirthday(LocalDate.parse(form.getBirthday(),
                    DateTimeFormatter.BASIC_ISO_DATE));
        }
        if (form.getOccupation() == null || form.getOccupation().isEmpty()) {
            user.setOccupation(null);
        } else {
            user.setOccupation(form.getOccupation());
        }
        user.setEmail(form.getEmail());
        return userRepository.save(user);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmailChanged(UserEditForm form, User currentUser) {
        return !currentUser.getEmail().equals(form.getEmail());
    }

    /** {@inheritDoc} */
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    /** {@inheritDoc} */
    @Override
    public void saveStripeCustomerId(User user, String customerId) {
        user.setStripeCustomerId(customerId);
        userRepository.save(user);
    }

    /** {@inheritDoc} */
    @Override
    public void updateRole(User user, String roleName) {
        Role role = roleRepository.findByName(roleName).orElseThrow();
        user.setRole(role);
        userRepository.save(user);
    }

    /** {@inheritDoc} */
    @Override
    public void refreshAuthenticationByRole(String newRole) {
        org.springframework.security.core.Authentication current =
                org.springframework.security.core.context.SecurityContextHolder
                        .getContext().getAuthentication();
        java.util.List<org.springframework.security.core.authority.SimpleGrantedAuthority> authorities =
                new java.util.ArrayList<>();
        authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority(newRole));
        org.springframework.security.core.Authentication newAuth =
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        current.getPrincipal(), current.getCredentials(), authorities);
        org.springframework.security.core.context.SecurityContextHolder.getContext()
                .setAuthentication(newAuth);
    }
}
