package com.example.nagoyameshi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.User;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    /**
     * 指定された店舗とユーザーに紐づくお気に入りを取得する。
     *
     * @param restaurantId 店舗ID
     * @param userId ユーザーID
     * @return 見つかったお気に入り
     */
    Optional<Favorite> findByRestaurantIdAndUserId(Long restaurantId, Long userId);

    /**
     * 指定ユーザーの全お気に入りを作成日時の降順で取得する。
     *
     * @param user     取得対象のユーザー
     * @param pageable ページ情報
     * @return お気に入りのページ
     */
    Page<Favorite> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
