package com.example.nagoyameshi.service;

import java.util.Optional;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.entity.VerificationToken;

/**
 * メール認証用トークンを扱うサービスインターフェース。
 * createVerificationToken はサインアップ時にトークンを保存するために利用し、
 * findVerificationTokenByToken はコントローラでトークンを検索するために利用する。
 */
public interface VerificationTokenService {
    /**
     * ユーザーとトークン文字列から VerificationToken を作成し保存する。
     *
     * @param user  対象のユーザー
     * @param token 保存するトークン
     */
    void createVerificationToken(User user, String token);

    /**
     * トークン文字列に一致する VerificationToken を取得する。
     *
     * @param token 検索対象のトークン
     * @return 見つかった VerificationToken。存在しない場合は空を返す
     */
    Optional<VerificationToken> findVerificationTokenByToken(String token);
}
