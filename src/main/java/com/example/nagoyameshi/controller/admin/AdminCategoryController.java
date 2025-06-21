package com.example.nagoyameshi.controller.admin;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.form.CategoryEditForm;
import com.example.nagoyameshi.form.CategoryRegisterForm;
import com.example.nagoyameshi.service.CategoryService;

import lombok.RequiredArgsConstructor;

/**
 * 管理者用のカテゴリ管理コントローラ。
 * 一覧表示・登録・更新・削除を提供する。
 */
@Controller
@RequiredArgsConstructor
public class AdminCategoryController {

    /** カテゴリ情報を扱うサービス */
    private final CategoryService categoryService;

    /**
     * カテゴリ一覧ページを表示する。
     *
     * @param pageable ページ情報
     * @param keyword  検索キーワード
     * @param model    ビューへ渡すモデル
     * @return 一覧画面テンプレート名
     */
    @GetMapping("/admin/categories")
    public String index(@PageableDefault(page = 0, size = 15, sort = "id", direction = Direction.ASC) Pageable pageable,
                        @RequestParam(name = "keyword", required = false) String keyword,
                        Model model) {
        Page<Category> categoryPage;
        if (keyword != null && !keyword.isBlank()) {
            categoryPage = categoryService.findCategoriesByNameLike(keyword, pageable);
        } else {
            categoryPage = categoryService.findAllCategories(pageable);
        }
        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryRegisterForm", new CategoryRegisterForm());
        model.addAttribute("categoryEditForm", new CategoryEditForm());
        return "admin/categories/index";
    }

    /**
     * カテゴリを新規登録する。
     */
    @PostMapping("/admin/categories/create")
    public String create(@Validated CategoryRegisterForm form, BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "入力内容に誤りがあります。");
            redirectAttributes.addFlashAttribute("categoryRegisterForm", form);
            return "redirect:/admin/categories";
        }
        categoryService.createCategory(form);
        redirectAttributes.addFlashAttribute("successMessage", "カテゴリを登録しました。");
        return "redirect:/admin/categories";
    }

    /**
     * 既存カテゴリを更新する。
     */
    @PostMapping("/admin/categories/{id}/update")
    public String update(@PathVariable("id") Long id, @Validated CategoryEditForm form,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        Optional<Category> categoryOpt = categoryService.findCategoryById(id);
        if (categoryOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "カテゴリが存在しません。");
            return "redirect:/admin/categories";
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "入力内容に誤りがあります。");
            return "redirect:/admin/categories";
        }
        categoryService.updateCategory(id, form);
        redirectAttributes.addFlashAttribute("successMessage", "カテゴリを更新しました。");
        return "redirect:/admin/categories";
    }

    /**
     * カテゴリを削除する。
     */
    @PostMapping("/admin/categories/{id}/delete")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Optional<Category> categoryOpt = categoryService.findCategoryById(id);
        if (categoryOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "カテゴリが存在しません。");
            return "redirect:/admin/categories";
        }
        categoryService.deleteCategory(id);
        redirectAttributes.addFlashAttribute("successMessage", "カテゴリを削除しました。");
        return "redirect:/admin/categories";
    }
}
