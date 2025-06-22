package com.example.nagoyameshi.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会員情報編集フォームの入力内容を保持するクラス。
 * 入力値検証用のアノテーションを付けてバリデーションを行う。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEditForm {
    /** 氏名 */
    @NotBlank(message = "氏名を入力してください。")
    private String name;

    /** フリガナ */
    @NotBlank(message = "フリガナを入力してください。")
    private String furigana;

    /** 郵便番号(7桁数字) */
    @NotBlank(message = "郵便番号を入力してください。")
    @Pattern(regexp = "^[0-9]{7}$", message = "郵便番号は7桁の半角数字で入力してください。")
    private String postalCode;

    /** 住所 */
    @NotBlank(message = "住所を入力してください。")
    private String address;

    /** 電話番号(10～11桁数字) */
    @NotBlank(message = "電話番号を入力してください。")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "電話番号は10桁または11桁の半角数字で入力してください。")
    private String phoneNumber;

    /** 誕生日 (任意: 空文字または8桁数字) */
    @Pattern(regexp = "^$|^[0-9]{8}$", message = "誕生日は8桁の半角数字で入力してください。")
    private String birthday;

    /** 職業 (任意) */
    private String occupation;

    /** メールアドレス */
    @NotBlank(message = "メールアドレスを入力してください。")
    @Email(message = "メールアドレスは正しい形式で入力してください。")
    private String email;
}
