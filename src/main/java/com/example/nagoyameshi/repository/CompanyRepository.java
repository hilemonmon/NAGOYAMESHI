package com.example.nagoyameshi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Company;

/**
 * 会社概要情報へアクセスするリポジトリインターフェース。
 */
public interface CompanyRepository extends JpaRepository<Company, Long> {

    /**
     * ID の降順で最初の会社情報を取得する。
     *
     * @return もっとも新しい会社情報
     */
    Optional<Company> findFirstByOrderByIdDesc();
}
