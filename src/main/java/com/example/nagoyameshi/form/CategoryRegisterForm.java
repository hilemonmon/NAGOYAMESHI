package com.example.nagoyameshi.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * カテゴリ登録時の入力内容を保持するフォーム。
 */
@Data
public class CategoryRegisterForm {
    /** カテゴリ名 */
    @NotBlank(message = "カテゴリ名を入力してください。")
    private String name;
}
