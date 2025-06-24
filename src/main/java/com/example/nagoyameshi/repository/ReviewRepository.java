package com.example.nagoyameshi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Review;

/**
 * レビュー情報を扱うリポジトリインターフェース。
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * 指定した店舗とユーザーのレビューを取得する。
     */
    Optional<Review> findByRestaurantIdAndUserId(Long restaurantId, Long userId);

    /**
     * 指定店舗のレビューを作成日時の降順で取得する。
     */
    Page<Review> findByRestaurantIdOrderByCreatedAtDesc(Long restaurantId, Pageable pageable);

    /**
     * ID の降順で最初の1件を取得する。
     */
    Optional<Review> findFirstByOrderByIdDesc();
}
