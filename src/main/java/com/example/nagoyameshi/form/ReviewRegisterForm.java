package com.example.nagoyameshi.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import lombok.Data;

/**
 * レビュー投稿フォームの入力内容を保持するクラス。
 * バリデーションで入力値の妥当性を確認する。
 */
@Data
public class ReviewRegisterForm {
    /** 評価(1~5) */
    @NotNull(message = "評価を選択してください。")
    @Range(min = 1, max = 5, message = "評価は1〜5で入力してください。")
    private Integer score;

    /** 感想 */
    @NotBlank(message = "感想を入力してください。")
    @Length(max = 300, message = "感想は300文字以内で入力してください。")
    private String content;
}
