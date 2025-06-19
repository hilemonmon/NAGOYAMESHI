package com.example.nagoyameshi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.entity.User;

/**
 * {@link UserRepository} の検索メソッドをテストするクラス。
 */
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("名前またはフリガナで部分一致検索できる")
    void 名前またはフリガナで部分一致検索できる() {
        // 役割を先に保存しユーザーに紐付ける
        Role role = roleRepository.save(Role.builder().name("ROLE_FREE_MEMBER").build());

        User u1 = User.builder()
                .name("山田太郎")
                .furigana("ヤマダタロウ")
                .email("yamada@example.com")
                .password("pass")
                .role(role)
                .build();
        User u2 = User.builder()
                .name("鈴木次郎")
                .furigana("スズキジロウ")
                .email("suzuki@example.com")
                .password("pass")
                .role(role)
                .build();
        userRepository.saveAll(List.of(u1, u2));

        Page<User> byName = userRepository
                .findByNameContainingIgnoreCaseOrFuriganaContainingIgnoreCase(
                        "山田", "dummy", PageRequest.of(0, 10));
        Page<User> byFurigana = userRepository
                .findByNameContainingIgnoreCaseOrFuriganaContainingIgnoreCase(
                        "dummy", "スズキ", PageRequest.of(0, 10));

        assertThat(byName.getContent()).hasSize(1);
        assertThat(byName.getContent().get(0).getEmail()).isEqualTo("yamada@example.com");
        assertThat(byFurigana.getContent()).hasSize(1);
        assertThat(byFurigana.getContent().get(0).getEmail()).isEqualTo("suzuki@example.com");
    }
}
