package com.example.nagoyameshi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * メールアドレスからユーザーを検索します。
     *
     * @param email メールアドレス
     * @return 該当ユーザー（存在しない場合は空）
     */
    Optional<User> findByEmail(String email);

    /**
     * 氏名またはフリガナで部分一致検索し、ページング結果を返します。
     * 大文字・小文字は区別しません。
     *
     * @param name     検索したい氏名
     * @param furigana 検索したいフリガナ
     * @param pageable ページ情報
     * @return 条件に合致したユーザーのページ
     */
    Page<User> findByNameContainingIgnoreCaseOrFuriganaContainingIgnoreCase(
            String name,
            String furigana,
            Pageable pageable);

    /**
     * 指定したロール名を持つユーザーの数を返します。
     *
     * @param roleName 取得したいロール名
     * @return 該当ユーザー数
     */
    long countByRole_Name(String roleName);
}
