package com.example.nagoyameshi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.form.RestaurantEditForm;
import com.example.nagoyameshi.form.RestaurantRegisterForm;

/**
 * 管理者向けに店舗情報を扱うサービスインターフェース。
 */
public interface AdminRestaurantService {

    /**
     * 検索キーワードを基に店舗一覧を取得する。
     * キーワードが空の場合は全件取得する。
     *
     * @param keyword  検索ワード（店舗名）
     * @param pageable ページ情報
     * @return 店舗のページ
     */
    Page<Restaurant> getRestaurants(String keyword, Pageable pageable);

    /**
     * ID を指定して店舗を1件取得する。
     *
     * @param id 店舗ID
     * @return 取得した店舗
     */
    Restaurant getRestaurant(Long id);

    /**
     * フォームの内容を基に店舗を登録する。
     *
     * @param form 入力された店舗情報
     * @return 登録した店舗
     */
    Restaurant create(RestaurantRegisterForm form);

    /**
     * フォーム内容を基に店舗を登録するエイリアスメソッド。
     * 課題で使用するため {@link #create(RestaurantRegisterForm)} と同じ動作を行う。
     *
     * @param form 入力された店舗情報
     * @return 登録した店舗
     */
    Restaurant createRestaurant(RestaurantRegisterForm form);

    /**
     * 価格設定が妥当か確認する。
     *
     * @param lowestPrice  最低価格
     * @param highestPrice 最高価格
     * @return 妥当なら true
     */
    boolean isValidPrices(Integer lowestPrice, Integer highestPrice);

    /**
     * 営業時間の設定が妥当か確認する。
     *
     * @param opening 開店時間
     * @param closing 閉店時間
     * @return 妥当なら true
     */
    boolean isValidBusinessHours(java.time.LocalTime opening, java.time.LocalTime closing);

    /**
     * 既存店舗を更新する。
     *
     * @param id   更新対象の店舗ID
     * @param form 入力された店舗情報
     * @return 更新後の店舗
     */
    Restaurant update(Long id, RestaurantEditForm form);

    /**
     * 指定した店舗を削除する。
     *
     * @param id 店舗ID
     */
    void delete(Long id);
}
