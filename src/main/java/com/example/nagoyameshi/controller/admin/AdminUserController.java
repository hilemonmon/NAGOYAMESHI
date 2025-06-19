package com.example.nagoyameshi.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.service.AdminUserService;

import lombok.RequiredArgsConstructor;

/**
 * 管理者用のユーザー管理コントローラ。
 * 一覧表示と詳細表示を提供する。
 */
@Controller
@RequiredArgsConstructor
public class AdminUserController {

    /** ユーザー情報を取得するサービス */
    private final AdminUserService adminUserService;

    /**
     * 会員一覧ページを表示する。
     *
     * @param keyword 検索キーワード（氏名またはフリガナ）
     * @param page    ページ番号（0開始）
     * @param model   画面へ渡すモデル
     * @return 一覧画面テンプレート名
     */
    @GetMapping("/admin/users")
    public String index(@RequestParam(name = "keyword", required = false) String keyword,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        Model model) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<User> userPage = adminUserService.searchUsers(keyword, pageable);
        model.addAttribute("userPage", userPage);
        model.addAttribute("keyword", keyword);
        return "admin/users/index";
    }

    /**
     * 会員詳細ページを表示する。
     *
     * @param id    ユーザーID
     * @param model 画面へ渡すモデル
     * @return 詳細画面テンプレート名
     */
    @GetMapping("/admin/users/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        User user = adminUserService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/users/show";
    }
}
