package com.example.nagoyameshi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Term;

/**
 * 利用規約情報へアクセスするリポジトリインターフェース。
 */
public interface TermRepository extends JpaRepository<Term, Long> {

    /**
     * ID の降順で最初の利用規約を取得する。
     *
     * @return 最新の利用規約
     */
    Optional<Term> findFirstByOrderByIdDesc();
}
