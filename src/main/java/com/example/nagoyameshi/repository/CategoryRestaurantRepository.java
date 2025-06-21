package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.nagoyameshi.entity.CategoryRestaurant;
import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Restaurant;

public interface CategoryRestaurantRepository extends JpaRepository<CategoryRestaurant, Integer> {
    /**
     * 指定した店舗に紐づくカテゴリIDを取得する。
     * idの昇順で並び替える。
     */
    @Query("SELECT cr.category.id FROM CategoryRestaurant cr WHERE cr.restaurant = :restaurant ORDER BY cr.id ASC")
    List<Integer> findCategoryIdsByRestaurantOrderByIdAsc(@Param("restaurant") Restaurant restaurant);

    /** 指定店舗とカテゴリの紐付けを取得する。 */
    CategoryRestaurant findByRestaurantAndCategory(Restaurant restaurant, Category category);

    /** 指定店舗に紐づくレコードをid昇順で取得する。 */
    List<CategoryRestaurant> findByRestaurantOrderByIdAsc(Restaurant restaurant);
}
