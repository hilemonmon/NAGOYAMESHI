package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByNameContaining(String name);

    /**
     * 店舗名で部分一致検索し、ページング結果を返します。
     *
     * @param name     検索キーワード
     * @param pageable ページ情報
     * @return 条件に合致した店舗のページ
     */
    Page<Restaurant> findByNameContaining(String name, Pageable pageable);
}
