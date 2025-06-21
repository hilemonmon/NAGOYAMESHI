package com.example.nagoyameshi.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会社概要編集フォームの入力内容を保持するクラス。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEditForm {
    /** 会社名 */
    @NotBlank(message = "会社名を入力してください。")
    private String name;

    /** 郵便番号(7桁数字) */
    @NotBlank(message = "郵便番号を入力してください。")
    @Pattern(regexp = "^[0-9]{7}$", message = "郵便番号は7桁の半角数字で入力してください。")
    private String postalCode;

    /** 住所 */
    @NotBlank(message = "住所を入力してください。")
    private String address;

    /** 代表者名 */
    @NotBlank(message = "代表者名を入力してください。")
    private String representative;

    /** 設立年月日 */
    @NotBlank(message = "設立年月日を入力してください。")
    private String establishmentDate;

    /** 資本金 */
    @NotBlank(message = "資本金を入力してください。")
    private String capital;

    /** 事業内容 */
    @NotBlank(message = "事業内容を入力してください。")
    private String business;

    /** 従業員数 */
    @NotBlank(message = "従業員数を入力してください。")
    private String numberOfEmployees;
}
