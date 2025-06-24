package com.example.nagoyameshi.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReviewEditForm;
import com.example.nagoyameshi.form.ReviewRegisterForm;

/**
 * レビュー情報を操作するサービスインターフェース。
 */
public interface ReviewService {

    /** ID を指定してレビューを取得する。 */
    Optional<Review> findReviewById(Long id);

    /** 指定店舗のレビューを作成日時降順で取得する。 */
    Page<Review> findReviewsByRestaurantOrderByCreatedAtDesc(Long restaurantId, Pageable pageable);

    /** レビュー件数を数える。 */
    long countReviews();

    /** 最新のレビューを取得する。 */
    Optional<Review> findFirstReviewByOrderByIdDesc();

    /** レビューを新規登録する。 */
    Review createReview(ReviewRegisterForm form, Restaurant restaurant, User user);

    /** レビュー内容を更新する。 */
    Review updateReview(Review review, ReviewEditForm form);

    /** レビューを削除する。 */
    void deleteReview(Review review);

    /** ユーザーが既に指定店舗へレビュー済みか判定する。 */
    boolean hasUserAlreadyReviewed(Long restaurantId, Long userId);
}
