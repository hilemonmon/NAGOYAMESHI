package com.example.nagoyameshi.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Company;
import com.example.nagoyameshi.form.CompanyEditForm;
import com.example.nagoyameshi.service.CompanyService;

import lombok.RequiredArgsConstructor;

/**
 * 管理者用の会社概要管理コントローラ。
 * 表示と更新機能のみを提供する。
 */
@Controller
@RequiredArgsConstructor
public class AdminCompanyController {

    /** 会社情報を扱うサービス */
    private final CompanyService companyService;

    /**
     * 会社概要ページを表示する。
     *
     * @param model 画面へ渡すモデル
     * @return テンプレート名
     */
    @GetMapping("/admin/company")
    public String index(Model model) {
        Company company = companyService.findFirstCompanyByOrderByIdDesc()
                .orElse(new Company());
        model.addAttribute("company", company);
        return "admin/company/index";
    }

    /**
     * 会社概要編集フォームを表示する。
     */
    @GetMapping("/admin/company/edit")
    public String edit(Model model) {
        Company company = companyService.findFirstCompanyByOrderByIdDesc()
                .orElse(new Company());
        CompanyEditForm form = new CompanyEditForm(
                company.getName(),
                company.getPostalCode(),
                company.getAddress(),
                company.getRepresentative(),
                company.getEstablishmentDate(),
                company.getCapital(),
                company.getBusiness(),
                company.getNumberOfEmployees());
        model.addAttribute("companyEditForm", form);
        return "admin/company/edit";
    }

    /**
     * 会社概要を更新する。
     */
    @PostMapping("/admin/company/update")
    public String update(@Validated CompanyEditForm form, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("companyEditForm", form);
            return "admin/company/edit";
        }
        companyService.updateCompany(form);
        redirectAttributes.addFlashAttribute("successMessage", "会社概要を更新しました。");
        return "redirect:/admin/company";
    }
}
