package com.example.nagoyameshi.controller;

import java.time.format.DateTimeFormatter;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.UserEditForm;
import com.example.nagoyameshi.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * 会員向けの情報表示・編集機能を提供するコントローラ。
 */
@Controller
@RequiredArgsConstructor
public class UserController {

    /** ユーザー関連の処理を行うサービス */
    private final UserService userService;

    /**
     * 会員情報ページを表示する。
     *
     * @param model 画面へ渡すモデル
     * @return 会員情報画面のテンプレート名
     */
    @GetMapping("/user")
    public String index(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByEmail(email);
        model.addAttribute("user", user);
        return "user/index";
    }

    /**
     * 会員情報編集ページを表示する。
     *
     * @param model 画面へ渡すモデル
     * @return 会員情報編集画面のテンプレート名
     */
    @GetMapping("/user/edit")
    public String edit(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByEmail(email);
        // 誕生日はフォーム用に文字列へ変換（nullはそのまま）
        String birthday = user.getBirthday() == null ? null
                : user.getBirthday().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        UserEditForm form = new UserEditForm(
                user.getName(),
                user.getFurigana(),
                user.getPostalCode(),
                user.getAddress(),
                user.getPhoneNumber(),
                birthday,
                user.getOccupation(),
                user.getEmail());
        model.addAttribute("userEditForm", form);
        return "user/edit";
    }

    /**
     * 会員情報を更新する。
     *
     * @param userEditForm      入力フォーム
     * @param bindingResult     バリデーション結果
     * @param redirectAttributes リダイレクト時に渡す属性
     * @param model             画面へ渡すモデル
     * @return エラー時は編集画面、成功時は会員情報画面へリダイレクト
     */
    @PostMapping("/user/update")
    public String update(@Validated UserEditForm userEditForm, BindingResult bindingResult,
                         RedirectAttributes redirectAttributes, Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findUserByEmail(email);

        // メールアドレスが変更されている場合は重複チェックを行う
        if (userService.isEmailChanged(userEditForm, currentUser)
                && userService.isEmailRegistered(userEditForm.getEmail())) {
            bindingResult.rejectValue("email", "email.registered", "既に登録済みのメールアドレスです。");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("userEditForm", userEditForm);
            return "user/edit";
        }

        userService.updateUser(userEditForm, currentUser);
        redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");
        return "redirect:/user";
    }
}
