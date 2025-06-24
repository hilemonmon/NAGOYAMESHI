package com.example.nagoyameshi.form;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;
import lombok.Data;

/**
 * 予約登録時の入力値を保持するフォームクラス。
 * 日付・時間・人数の3項目のみを扱う。
 */
@Data
public class ReservationRegisterForm {
    /** 予約日 */
    @NotNull(message = "予約日を入力してください。")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reservationDate;

    /** 予約時間 */
    @NotNull(message = "時間を選択してください。")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime reservationTime;

    /** 人数 */
    @NotNull(message = "人数を選択してください。")
    @Range(min = 1, max = 50, message = "1〜50名で入力してください。")
    private Integer numberOfPeople;
}
