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

    /**
     * 最低価格の昇順で店舗を取得します。
     *
     * @param pageable ページ情報
     * @return 店舗ページ
     */
    Page<Restaurant> findAllRestaurantsByOrderByLowestPriceAsc(Pageable pageable);

    /**
     * キーワード検索（店舗名・住所・カテゴリ名）を作成日降順で行います。
     *
     * @param keyword  検索ワード
     * @param pageable ページ情報
     * @return 検索結果ページ
     */
    Page<Restaurant> findRestaurantsByNameLikeOrAddressLikeOrCategoryNameLikeOrderByCreatedAtDesc(String keyword,
            Pageable pageable);

    /**
     * キーワード検索（店舗名・住所・カテゴリ名）を最低価格昇順で行います。
     *
     * @param keyword  検索ワード
     * @param pageable ページ情報
     * @return 検索結果ページ
     */
    Page<Restaurant> findRestaurantsByNameLikeOrAddressLikeOrCategoryNameLikeOrderByLowestPriceAsc(String keyword,
            Pageable pageable);

    /**
     * カテゴリIDで検索し作成日降順で並べます。
     */
    Page<Restaurant> findRestaurantsByCategoryIdOrderByCreatedAtDesc(Integer categoryId, Pageable pageable);

    /**
     * カテゴリIDで検索し最低価格昇順で並べます。
     */
    Page<Restaurant> findRestaurantsByCategoryIdOrderByLowestPriceAsc(Integer categoryId, Pageable pageable);

    /**
     * 指定価格以下の店舗を作成日降順で取得します。
     */
    Page<Restaurant> findRestaurantsByLowestPriceLessThanEqualOrderByCreatedAtDesc(Integer price, Pageable pageable);

    /**
     * 指定価格以下の店舗を最低価格昇順で取得します。
     */
    Page<Restaurant> findRestaurantsByLowestPriceLessThanEqualOrderByLowestPriceAsc(Integer price, Pageable pageable);

    /** 平均評価の降順で店舗を取得する。 */
    Page<Restaurant> findAllRestaurantsByOrderByAverageScoreDesc(Pageable pageable);

    /** キーワード検索を行い平均評価の降順で店舗を取得する。 */
    Page<Restaurant> findRestaurantsByNameLikeOrAddressLikeOrCategoryNameLikeOrderByAverageScoreDesc(String keyword,
            Pageable pageable);

    /** カテゴリID指定で平均評価の降順に並べる。 */
    Page<Restaurant> findRestaurantsByCategoryIdOrderByAverageScoreDesc(Integer categoryId, Pageable pageable);

    /** 予算上限以下で平均評価の降順に並べる。 */
    Page<Restaurant> findRestaurantsByLowestPriceLessThanEqualOrderByAverageScoreDesc(Integer price,
            Pageable pageable);

    /**
     * 指定 ID の店舗を取得する。
     *
     * @param id 店舗 ID
     * @return 見つかった店舗情報
     */
    java.util.Optional<Restaurant> findRestaurantById(Long id);
}
