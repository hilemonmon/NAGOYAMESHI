package com.example.nagoyameshi.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReviewEditForm;
import com.example.nagoyameshi.form.ReviewRegisterForm;
import com.example.nagoyameshi.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

/**
 * {@link ReviewService} の実装クラス。
 */
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    /** {@inheritDoc} */
    @Override
    public Optional<Review> findReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Review> findReviewsByRestaurantOrderByCreatedAtDesc(Long restaurantId, Pageable pageable) {
        return reviewRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId, pageable);
    }

    /** {@inheritDoc} */
    @Override
    public long countReviews() {
        return reviewRepository.count();
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Review> findFirstReviewByOrderByIdDesc() {
        return reviewRepository.findFirstByOrderByIdDesc();
    }

    /** {@inheritDoc} */
    @Override
    public Review createReview(ReviewRegisterForm form, Restaurant restaurant, User user) {
        Review review = Review.builder()
                .score(form.getScore())
                .content(form.getContent())
                .restaurant(restaurant)
                .user(user)
                .build();
        return reviewRepository.save(review);
    }

    /** {@inheritDoc} */
    @Override
    public Review updateReview(Review review, ReviewEditForm form) {
        review.setScore(form.getScore());
        review.setContent(form.getContent());
        return reviewRepository.save(review);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteReview(Review review) {
        reviewRepository.delete(review);
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasUserAlreadyReviewed(Long restaurantId, Long userId) {
        return reviewRepository.findByRestaurantIdAndUserId(restaurantId, userId).isPresent();
    }
}
