package com.example.nagoyameshi.service;

import com.example.nagoyameshi.dto.RegisterRequest;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.SignupForm;
import com.example.nagoyameshi.form.UserEditForm;

public interface UserService {
    User register(RegisterRequest request);

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
    /**
     * 会員登録フォームからユーザーを生成して保存する。
     * 保存時点ではメール認証前のため enabled は false とする。
     *
     * @param form 入力された会員情報
     * @return 保存したユーザーエンティティ
     */
    User createUser(SignupForm form);

    /**
     * 指定したユーザーを有効化する。
     *
     * @param user 有効化したいユーザー
     */
    void enableUser(User user);

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

    /**
     * 入力フォームの内容でユーザー情報を更新する。
     *
     * @param form  入力された会員情報
     * @param user  更新対象のユーザー
     * @return 更新後のユーザーエンティティ
     */
    User updateUser(UserEditForm form, User user);

    /**
     * フォームに入力されたメールアドレスが現在のユーザーと異なるか確認する。
     *
     * @param form        入力フォーム
     * @param currentUser 現在のユーザー
     * @return 変更されていれば true
     */
    boolean isEmailChanged(UserEditForm form, User currentUser);

    /**
     * メールアドレスからユーザーを検索する。
     *
     * @param email メールアドレス
     * @return 該当ユーザー（存在しない場合は null）
     */
    User findUserByEmail(String email);

    /**
     * Stripeの顧客IDをユーザーに保存する。
     *
     * @param user       更新対象のユーザー
     * @param customerId Stripeで発行された顧客ID
     */
    void saveStripeCustomerId(User user, String customerId);

    /**
     * ユーザーのロールを更新する。
     *
     * @param user     更新対象のユーザー
     * @param roleName 更新後のロール名
     */
    void updateRole(User user, String roleName);

    /**
     * ロール変更後に現在の認証情報を更新する。
     *
     * @param newRole 新しいロール名
     */
    void refreshAuthenticationByRole(String newRole);
}
