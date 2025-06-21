package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.nagoyameshi.entity.RegularHoliday;
import com.example.nagoyameshi.entity.RegularHolidayRestaurant;
import com.example.nagoyameshi.entity.Restaurant;

/**
 * 店舗と定休日の中間テーブルへアクセスするリポジトリ。
 */
public interface RegularHolidayRestaurantRepository extends JpaRepository<RegularHolidayRestaurant, Integer> {

    /**
     * 指定した店舗に紐づく定休日ID一覧を取得する。
     *
     * @param restaurant 対象の店舗
     * @return 定休日IDのリスト
     */
    @Query("SELECT rr.regularHoliday.id FROM RegularHolidayRestaurant rr WHERE rr.restaurant = :restaurant")
    List<Integer> findRegularHolidayIdsByRestaurant(@Param("restaurant") Restaurant restaurant);

    /** 指定店舗と定休日の紐付けを取得する。 */
    RegularHolidayRestaurant findByRestaurantAndRegularHoliday(Restaurant restaurant, RegularHoliday regularHoliday);

    /** 指定店舗に紐づくレコードをid昇順で取得する。 */
    List<RegularHolidayRestaurant> findByRestaurantOrderByIdAsc(Restaurant restaurant);
}
