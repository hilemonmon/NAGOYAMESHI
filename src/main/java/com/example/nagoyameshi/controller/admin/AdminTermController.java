package com.example.nagoyameshi.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Term;
import com.example.nagoyameshi.form.TermEditForm;
import com.example.nagoyameshi.service.TermService;

import lombok.RequiredArgsConstructor;

/**
 * 管理者用の利用規約管理コントローラ。
 */
@Controller
@RequiredArgsConstructor
public class AdminTermController {

    /** 利用規約を扱うサービス */
    private final TermService termService;

    /**
     * 利用規約ページを表示する。
     */
    @GetMapping("/admin/terms")
    public String index(Model model) {
        Term term = termService.findFirstTermByOrderByIdDesc()
                .orElse(new Term());
        model.addAttribute("term", term);
        return "admin/terms/index";
    }

    /**
     * 利用規約編集フォームを表示する。
     */
    @GetMapping("/admin/terms/edit")
    public String edit(Model model) {
        Term term = termService.findFirstTermByOrderByIdDesc()
                .orElse(new Term());
        TermEditForm form = new TermEditForm(term.getContent());
        model.addAttribute("termEditForm", form);
        return "admin/terms/edit";
    }

    /**
     * 利用規約を更新する。
     */
    @PostMapping("/admin/terms/update")
    public String update(@Validated TermEditForm form, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("termEditForm", form);
            return "admin/terms/edit";
        }
        termService.updateTerm(form);
        redirectAttributes.addFlashAttribute("successMessage", "利用規約を更新しました。");
        return "redirect:/admin/terms";
    }
}
