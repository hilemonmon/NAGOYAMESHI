package com.example.nagoyameshi.form;

import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 店舗登録フォームの入力内容を保持するクラス。
 * バリデーションアノテーションで入力チェックを行う。
 */
@Data
public class RestaurantRegisterForm {
    /** 店舗名 */
    @NotBlank(message = "店舗名を入力してください。")
    private String name;

    /** 画像ファイル (任意) */
    private MultipartFile imageFile;

    /** 説明 */
    @NotBlank(message = "説明を入力してください。")
    private String description;

    /** 最低価格 */
    @NotNull(message = "最低価格を入力してください。")
    private Integer lowestPrice;

    /** 最高価格 */
    @NotNull(message = "最高価格を入力してください。")
    private Integer highestPrice;

    /** 郵便番号 */
    @NotBlank(message = "郵便番号を入力してください。")
    @Pattern(regexp = "^[0-9]{7}$", message = "郵便番号は7桁の半角数字で入力してください。")
    private String postalCode;

    /** 住所 */
    @NotBlank(message = "住所を入力してください。")
    private String address;

    /** 開店時間 */
    @NotNull(message = "開店時間を入力してください。")
    @DateTimeFormat(pattern = "H:mm")
    private LocalTime openingTime;

    /** 閉店時間 */
    @NotNull(message = "閉店時間を入力してください。")
    @DateTimeFormat(pattern = "H:mm")
    private LocalTime closingTime;

    /** 座席数 */
    @NotNull(message = "座席数を入力してください。")
    private Integer seatingCapacity;
}
