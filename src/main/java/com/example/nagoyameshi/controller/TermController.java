package com.example.nagoyameshi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagoyameshi.entity.Term;
import com.example.nagoyameshi.repository.TermRepository;

import lombok.RequiredArgsConstructor;

/**
 * 会員向けの利用規約ページを表示するコントローラ。
 */
@Controller
@RequiredArgsConstructor
public class TermController {

    /** データベースアクセス用のリポジトリ */
    private final TermRepository termRepository;

    /**
     * 利用規約ページを表示する。
     *
     * @param model ビューへ渡すモデル
     * @return テンプレート名
     */
    @GetMapping("/terms")
    public String index(Model model) {
        // 最新の利用規約を1件取得し、存在しない場合は空の Term を渡す
        Term term = termRepository.findFirstByOrderByIdDesc()
                .orElse(new Term());
        model.addAttribute("term", term);
        return "terms/index";
    }
}
