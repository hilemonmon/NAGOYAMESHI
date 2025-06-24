package com.example.nagoyameshi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagoyameshi.entity.Company;
import com.example.nagoyameshi.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;

/**
 * 会員向けの会社概要ページを表示するコントローラ。
 * 最新の会社情報を取得してビューへ渡すだけのシンプルな処理を行う。
 */
@Controller
@RequiredArgsConstructor
public class CompanyController {

    /** データベースアクセス用のリポジトリ */
    private final CompanyRepository companyRepository;

    /**
     * 会社概要ページを表示する。
     * 未ログインでも閲覧可能なページのため、アクセス制御は {@link com.example.nagoyameshi.security.WebSecurityConfig} で行う。
     *
     * @param model ビューへ値を渡すモデル
     * @return テンプレート名
     */
    @GetMapping("/company")
    public String index(Model model) {
        // ID の降順で1件だけ取得。存在しない場合は空の Company を渡す。
        Company company = companyRepository.findFirstByOrderByIdDesc()
                .orElse(new Company());
        model.addAttribute("company", company);
        return "company/index";
    }
}
