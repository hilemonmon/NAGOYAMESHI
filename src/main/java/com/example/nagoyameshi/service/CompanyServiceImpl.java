package com.example.nagoyameshi.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Company;
import com.example.nagoyameshi.form.CompanyEditForm;
import com.example.nagoyameshi.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;

/**
 * {@link CompanyService} の実装クラス。
 */
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    /** データベースアクセス用リポジトリ */
    private final CompanyRepository companyRepository;

    /** {@inheritDoc} */
    @Override
    public Optional<Company> findFirstCompanyByOrderByIdDesc() {
        return companyRepository.findFirstByOrderByIdDesc();
    }

    /** {@inheritDoc} */
    @Override
    public Company updateCompany(CompanyEditForm form) {
        Company company = companyRepository.findFirstByOrderByIdDesc()
                .orElseGet(Company::new);
        company.setName(form.getName());
        company.setPostalCode(form.getPostalCode());
        company.setAddress(form.getAddress());
        company.setRepresentative(form.getRepresentative());
        company.setEstablishmentDate(form.getEstablishmentDate());
        company.setCapital(form.getCapital());
        company.setBusiness(form.getBusiness());
        company.setNumberOfEmployees(form.getNumberOfEmployees());
        return companyRepository.save(company);
    }
}
