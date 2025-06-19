package com.example.nagoyameshi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.nagoyameshi.entity.User;

/**
 * 管理者向けにユーザー情報を扱うサービスインターフェース。
 */
public interface AdminUserService {

    /**
     * 検索キーワードを基にユーザー一覧を取得する。
     * キーワードが空の場合は全件取得する。
     *
     * @param keyword  検索ワード（氏名またはフリガナ）
     * @param pageable ページ情報
     * @return ユーザーのページ
     */
    Page<User> searchUsers(String keyword, Pageable pageable);

    /**
     * ID を指定してユーザーを1件取得する。
     *
     * @param id ユーザーID
     * @return 取得したユーザー
     */
    User getUserById(Long id);
}
