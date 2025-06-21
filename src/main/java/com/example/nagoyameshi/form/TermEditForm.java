package com.example.nagoyameshi.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 利用規約編集フォームの入力内容を保持するクラス。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TermEditForm {
    /** 利用規約本文 */
    @NotBlank(message = "利用規約を入力してください。")
    private String content;
}
