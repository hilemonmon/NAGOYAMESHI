package com.example.nagoyameshi.service;

import java.util.Optional;

import com.example.nagoyameshi.entity.Company;
import com.example.nagoyameshi.form.CompanyEditForm;

/**
 * 会社概要を操作するサービスインターフェース。
 */
public interface CompanyService {

    /** 最新の会社概要を取得する。 */
    Optional<Company> findFirstCompanyByOrderByIdDesc();

    /** 会社概要を更新する。 */
    Company updateCompany(CompanyEditForm form);
}
