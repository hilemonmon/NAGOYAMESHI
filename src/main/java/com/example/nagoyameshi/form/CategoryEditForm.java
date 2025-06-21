package com.example.nagoyameshi.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * カテゴリ編集時の入力内容を保持するフォーム。
 */
@Data
public class CategoryEditForm {
    /** カテゴリ名 */
    @NotBlank(message = "カテゴリ名を入力してください。")
    private String name;
}
