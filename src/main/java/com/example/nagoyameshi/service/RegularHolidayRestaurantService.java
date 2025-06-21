package com.example.nagoyameshi.service;

import java.util.List;

import com.example.nagoyameshi.entity.Restaurant;

/**
 * 店舗と定休日の関連を操作するサービス。
 */
public interface RegularHolidayRestaurantService {

    /** 指定店舗に紐づく定休日ID一覧を取得する。 */
    List<Integer> findRegularHolidayIdsByRestaurant(Restaurant restaurant);

    /** 定休日IDの集合を元に中間エンティティを作成する。 */
    void createRegularHolidaysRestaurants(List<Integer> regularHolidayIds, Restaurant restaurant);

    /** 既存の紐付けとフォーム入力を同期する。 */
    void syncRegularHolidaysRestaurants(List<Integer> regularHolidayIds, Restaurant restaurant);
}
