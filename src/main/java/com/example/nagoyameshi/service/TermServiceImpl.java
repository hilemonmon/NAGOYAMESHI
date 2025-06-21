package com.example.nagoyameshi.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Term;
import com.example.nagoyameshi.form.TermEditForm;
import com.example.nagoyameshi.repository.TermRepository;

import lombok.RequiredArgsConstructor;

/**
 * {@link TermService} の実装クラス。
 */
@Service
@RequiredArgsConstructor
public class TermServiceImpl implements TermService {

    /** データベースアクセス用リポジトリ */
    private final TermRepository termRepository;

    /** {@inheritDoc} */
    @Override
    public Optional<Term> findFirstTermByOrderByIdDesc() {
        return termRepository.findFirstByOrderByIdDesc();
    }

    /** {@inheritDoc} */
    @Override
    public Term updateTerm(TermEditForm form) {
        Term term = termRepository.findFirstByOrderByIdDesc()
                .orElseGet(Term::new);
        term.setContent(form.getContent());
        return termRepository.save(term);
    }
}
