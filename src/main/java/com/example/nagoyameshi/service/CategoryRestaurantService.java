package com.example.nagoyameshi.service;

import java.util.List;

import com.example.nagoyameshi.entity.Restaurant;

/**
 * レストランとカテゴリの中間テーブルを操作するサービス。
 */
public interface CategoryRestaurantService {

    /**
     * 指定した店舗に紐づくカテゴリID一覧を取得する。
     * @param restaurant 対象の店舗
     * @return カテゴリIDのリスト
     */
    List<Integer> findCategoryIdsByRestaurantOrderByIdAsc(Restaurant restaurant);

    /**
     * 指定したカテゴリID群を店舗へ紐付けて保存する。
     * @param categoryIds 追加したいカテゴリID
     * @param restaurant  対象の店舗
     */
    void createCategoriesRestaurants(List<Integer> categoryIds, Restaurant restaurant);

    /**
     * 店舗に紐付くカテゴリを指定IDの集合と同期する。
     * @param categoryIds フォームから送信されたカテゴリID
     * @param restaurant  対象の店舗
     */
    void syncCategoriesRestaurants(List<Integer> categoryIds, Restaurant restaurant);
}
