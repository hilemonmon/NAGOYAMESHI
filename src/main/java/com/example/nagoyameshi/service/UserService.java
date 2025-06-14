package com.example.nagoyameshi.service;

import com.example.nagoyameshi.dto.RegisterRequest;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.SignupForm;

public interface UserService {
    User register(RegisterRequest request);
    boolean verify(String token);

    /**
     * 認証トークンを生成して保存する。
     *
     * @param user  対象のユーザー
     * @param token 保存するトークン
     */
    void createVerificationToken(User user, String token);

    /**
     * フォームの内容を基にユーザーを作成し保存する。
     *
     * @param form 入力された会員情報
     */
    void createUser(SignupForm form);

    /**
     * メールアドレスがすでに登録済みか判定する。
     *
     * @param email 確認したいメールアドレス
     * @return 登録済みであれば true
     */
    boolean isEmailRegistered(String email);

    /**
     * 2つのパスワードが一致するか判定する。
     *
     * @param password              パスワード
     * @param passwordConfirmation  確認用パスワード
     * @return 一致していれば true
     */
    boolean isSamePassword(String password, String passwordConfirmation);
}
