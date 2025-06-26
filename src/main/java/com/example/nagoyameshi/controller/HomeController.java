package com.example.nagoyameshi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpServletRequest;

import com.example.nagoyameshi.form.SignupForm;
import com.example.nagoyameshi.service.UserService;
import com.example.nagoyameshi.service.VerificationTokenService;
import com.example.nagoyameshi.service.RestaurantService;
import com.example.nagoyameshi.service.CategoryService;
import org.springframework.data.domain.PageRequest;
import com.example.nagoyameshi.event.SignupEventPublisher;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    /** ユーザー関連の処理を行うサービス */
    private final UserService userService;
    /** 認証トークンを扱うサービス */
    private final VerificationTokenService verificationTokenService;
    /** サインアップ完了時のイベント発行クラス */
    private final SignupEventPublisher signupEventPublisher;
    /** 店舗関連の処理を行うサービス */
    private final RestaurantService restaurantService;
    /** カテゴリ関連の処理を行うサービス */
    private final CategoryService categoryService;

    @GetMapping("/")
    public String index(Model model) {
        // 管理者としてログインしている場合は管理画面へリダイレクト
        var auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !(auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
            var user = userService.findUserByEmail(auth.getName());
            if (user != null && "ROLE_ADMIN".equals(user.getRole().getName())) {
                return "redirect:/admin";
            }
        }

        // 評価が高い店舗: レビュー平均点の高い順に6件取得
        var highlyRatedRestaurants = restaurantService
                .findAllRestaurantsByOrderByAverageScoreDesc(PageRequest.of(0, 6))
                .getContent();

        // 新規掲載店: 作成日時の降順で6件取得
        var newRestaurants = restaurantService
                .findAllRestaurantsByOrderByCreatedAtDesc(PageRequest.of(0, 6))
                .getContent();

        // 特定カテゴリの取得
        var washoku = categoryService.findFirstCategoryByName("和食").orElse(null);
        var udon = categoryService.findFirstCategoryByName("うどん").orElse(null);
        var don = categoryService.findFirstCategoryByName("丼物").orElse(null);
        var ramen = categoryService.findFirstCategoryByName("ラーメン").orElse(null);
        var oden = categoryService.findFirstCategoryByName("おでん").orElse(null);
        var fried = categoryService.findFirstCategoryByName("揚げ物").orElse(null);

        // 全カテゴリ一覧
        var categories = categoryService.findAllCategories();

        // 取得したデータをビューへ渡す
        model.addAttribute("highlyRatedRestaurants", highlyRatedRestaurants);
        model.addAttribute("newRestaurants", newRestaurants);
        model.addAttribute("washoku", washoku);
        model.addAttribute("udon", udon);
        model.addAttribute("don", don);
        model.addAttribute("ramen", ramen);
        model.addAttribute("oden", oden);
        model.addAttribute("fried", fried);
        model.addAttribute("categories", categories);

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
            RedirectAttributes redirectAttributes, HttpServletRequest request) {

        if (userService.isEmailRegistered(signupForm.getEmail())) {
            bindingResult.rejectValue("email", "email.registered", "既に登録済みのメールアドレスです。");
        }
        if (!userService.isSamePassword(signupForm.getPassword(), signupForm.getPasswordConfirmation())) {
            bindingResult.rejectValue("passwordConfirmation", "password.unmatch", "パスワードが一致しません。");
        }

        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        // ユーザー登録（enabled=false で保存）
        var user = userService.createUser(signupForm);

        // リクエストURLからドメイン部分を取得しイベントへ渡す
        String baseUrl = request.getRequestURL().toString().replace("/signup", "");
        signupEventPublisher.publish(user, baseUrl);

        redirectAttributes.addFlashAttribute("successMessage",
                "ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、会員登録を完了してください。");
        return "redirect:/";
    }

    /**
     * メール認証URLから呼び出される確認処理。
     *
     * @param token 認証トークン
     * @param model ビューへ値を渡すモデル
     * @return 認証結果表示画面
     */
    @GetMapping("/signup/verify")
    public String verify(@RequestParam("token") String token, Model model) {
        var vToken = verificationTokenService.findVerificationTokenByToken(token);
        if (vToken.isPresent()) {
            // トークンが見つかった場合はユーザーを有効化
            userService.enableUser(vToken.get().getUser());
            model.addAttribute("successMessage", "会員登録が完了しました。");
        } else {
            model.addAttribute("errorMessage", "トークンが無効です。");
        }
        return "auth/verify";
    }
}
