package com.example.nagoyameshi.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;

/**
 * お気に入り情報を操作するサービスインターフェース。
 */
public interface FavoriteService {

    /** IDを指定してお気に入りを取得する。 */
    Optional<Favorite> findFavoriteById(Long id);

    /** 指定店舗とユーザーの組み合わせでお気に入りを取得する。 */
    Optional<Favorite> findFavoriteByRestaurantAndUser(Restaurant restaurant, User user);

    /** 指定ユーザーの全お気に入りを作成日時降順で取得する。 */
    Page<Favorite> findFavoritesByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    /** お気に入り件数を数える。 */
    long countFavorites();

    /** 店舗とユーザーを指定してお気に入りを登録する。 */
    Favorite createFavorite(Restaurant restaurant, User user);

    /** お気に入りを削除する。 */
    void deleteFavorite(Favorite favorite);

    /** 指定店舗が既にお気に入り登録されているか判定する。 */
    boolean isFavorite(Restaurant restaurant, User user);
}
