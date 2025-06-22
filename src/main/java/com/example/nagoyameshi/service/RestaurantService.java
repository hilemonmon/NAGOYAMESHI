package com.example.nagoyameshi.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.nagoyameshi.entity.Restaurant;

public interface RestaurantService {
    List<Restaurant> getRestaurants(String name);

    /**
     * 作成日の降順で店舗を取得します。
     *
     * @param pageable ページ情報
     * @return 店舗ページ
     */
    Page<Restaurant> findAllRestaurantsByOrderByCreatedAtDesc(Pageable pageable);
}
