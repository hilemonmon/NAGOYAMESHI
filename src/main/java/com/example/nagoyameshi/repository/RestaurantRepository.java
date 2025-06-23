package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    /**
     * 作成日の降順で全ての店舗を取得し、ページング結果を返します。
     *
     * @param pageable ページ情報
     * @return 作成日の降順で並んだ店舗ページ
     */
    Page<Restaurant> findAllByOrderByCreatedAtDesc(Pageable pageable);

    /**
     * 最低価格の昇順で全ての店舗を取得します。
     *
     * @param pageable ページ情報
     * @return 価格が安い順に並んだ店舗ページ
     */
    Page<Restaurant> findAllByOrderByLowestPriceAsc(Pageable pageable);

    /**
     * キーワードで店舗名・住所・カテゴリ名を部分一致検索し、作成日の降順で取得します。
     * カテゴリ名検索のためにカテゴリテーブルと結合します。
     *
     * @param name         店舗名キーワード
     * @param address      住所キーワード
     * @param categoryName カテゴリ名キーワード
     * @param pageable     ページ情報
     * @return 検索結果ページ
     */
    @Query("""
            SELECT DISTINCT r FROM Restaurant r
            LEFT JOIN r.categoriesRestaurants cr
            LEFT JOIN cr.category c
            WHERE r.name LIKE %:name%
               OR r.address LIKE %:address%
               OR c.name LIKE %:categoryName%
            ORDER BY r.createdAt DESC
            """)
    Page<Restaurant> findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByCreatedAtDesc(
            @Param("name") String name,
            @Param("address") String address,
            @Param("categoryName") String categoryName,
            Pageable pageable);

    /**
     * キーワードで店舗名・住所・カテゴリ名を部分一致検索し、最低価格の昇順で取得します。
     *
     * @param name         店舗名キーワード
     * @param address      住所キーワード
     * @param categoryName カテゴリ名キーワード
     * @param pageable     ページ情報
     * @return 検索結果ページ
     */
    @Query("""
            SELECT DISTINCT r FROM Restaurant r
            LEFT JOIN r.categoriesRestaurants cr
            LEFT JOIN cr.category c
            WHERE r.name LIKE %:name%
               OR r.address LIKE %:address%
               OR c.name LIKE %:categoryName%
            ORDER BY r.lowestPrice ASC
            """)
    Page<Restaurant> findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByLowestPriceAsc(
            @Param("name") String name,
            @Param("address") String address,
            @Param("categoryName") String categoryName,
            Pageable pageable);

    /**
     * カテゴリIDで検索し、作成日の降順で店舗を取得します。
     *
     * @param categoryId カテゴリID
     * @param pageable   ページ情報
     * @return 検索結果ページ
     */
    @Query("""
            SELECT DISTINCT r FROM Restaurant r
            JOIN r.categoriesRestaurants cr
            WHERE cr.category.id = :categoryId
            ORDER BY r.createdAt DESC
            """)
    Page<Restaurant> findByCategoryIdOrderByCreatedAtDesc(
            @Param("categoryId") Integer categoryId,
            Pageable pageable);

    /**
     * カテゴリIDで検索し、最低価格の昇順で店舗を取得します。
     *
     * @param categoryId カテゴリID
     * @param pageable   ページ情報
     * @return 検索結果ページ
     */
    @Query("""
            SELECT DISTINCT r FROM Restaurant r
            JOIN r.categoriesRestaurants cr
            WHERE cr.category.id = :categoryId
            ORDER BY r.lowestPrice ASC
            """)
    Page<Restaurant> findByCategoryIdOrderByLowestPriceAsc(
            @Param("categoryId") Integer categoryId,
            Pageable pageable);

    /**
     * 指定した価格以下の店舗を作成日の降順で取得します。
     *
     * @param price    予算上限
     * @param pageable ページ情報
     * @return 検索結果ページ
     */
    Page<Restaurant> findByLowestPriceLessThanEqualOrderByCreatedAtDesc(Integer price, Pageable pageable);

    /**
     * 指定した価格以下の店舗を最低価格の昇順で取得します。
     *
     * @param price    予算上限
     * @param pageable ページ情報
     * @return 検索結果ページ
     */
    Page<Restaurant> findByLowestPriceLessThanEqualOrderByLowestPriceAsc(Integer price, Pageable pageable);
}
