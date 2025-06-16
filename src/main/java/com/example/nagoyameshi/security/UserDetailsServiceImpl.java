package com.example.nagoyameshi.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * メールアドレスからユーザー情報を取得し、UserDetails を生成するサービスです。
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    /** ユーザー情報にアクセスするリポジトリ */
    private final UserRepository userRepository;

    /**
     * 指定されたメールアドレスに一致するユーザーを検索し、
     * UserDetailsImpl オブジェクトに変換して返します。
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String roleName = user.getRole().getName();
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(authority);

            return new UserDetailsImpl(user, authorities);
        } catch (Exception e) {
            // ユーザー取得に失敗した場合は UsernameNotFoundException をスロー
            throw new UsernameNotFoundException("User not found", e);
        }
    }
}
