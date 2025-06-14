package com.example.nagoyameshi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.form.SignupForm;
import com.example.nagoyameshi.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {

    /** ユーザー関連の処理を行うサービス */
    private final UserService userService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * ログインページを表示する。
     */
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    /**
     * 会員登録ページを表示する。
     *
     * @param signupForm フォームオブジェクト
     * @return 会員登録画面
     */
    @GetMapping("/signup")
    public String signup(SignupForm signupForm) {
        return "auth/signup";
    }

    /**
     * 会員登録処理を行う。
     *
     * @param signupForm         フォーム入力値
     * @param bindingResult      バリデーション結果
     * @param redirectAttributes リダイレクト時に渡す属性
     * @return エラー時は登録画面、成功時はトップページへリダイレクト
     */
    @PostMapping("/signup")
    public String signup(@Validated SignupForm signupForm, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (userService.isEmailRegistered(signupForm.getEmail())) {
            bindingResult.rejectValue("email", "email.registered", "既に登録済みのメールアドレスです。");
        }
        if (!userService.isSamePassword(signupForm.getPassword(), signupForm.getPasswordConfirmation())) {
            bindingResult.rejectValue("passwordConfirmation", "password.unmatch", "パスワードが一致しません。");
        }

        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        userService.createUser(signupForm);
        redirectAttributes.addFlashAttribute("successMessage", "会員登録が完了しました。");
        return "redirect:/";
    }
}
