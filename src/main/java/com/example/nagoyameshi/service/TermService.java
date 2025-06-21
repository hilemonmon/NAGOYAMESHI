package com.example.nagoyameshi.service;

import java.util.Optional;

import com.example.nagoyameshi.entity.Term;
import com.example.nagoyameshi.form.TermEditForm;

/**
 * 利用規約を操作するサービスインターフェース。
 */
public interface TermService {

    /** 最新の利用規約を取得する。 */
    Optional<Term> findFirstTermByOrderByIdDesc();

    /** 利用規約を更新する。 */
    Term updateTerm(TermEditForm form);
}
