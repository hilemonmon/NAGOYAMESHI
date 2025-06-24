package com.example.nagoyameshi.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.FavoriteRepository;

import lombok.RequiredArgsConstructor;

/**
 * {@link FavoriteService} の実装クラス。
 */
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;

    /** {@inheritDoc} */
    @Override
    public Optional<Favorite> findFavoriteById(Long id) {
        return favoriteRepository.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Favorite> findFavoriteByRestaurantAndUser(Restaurant restaurant, User user) {
        return favoriteRepository.findByRestaurantIdAndUserId(restaurant.getId(), user.getId());
    }

    /** {@inheritDoc} */
    @Override
    public Page<Favorite> findFavoritesByUserOrderByCreatedAtDesc(User user, Pageable pageable) {
        return favoriteRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }

    /** {@inheritDoc} */
    @Override
    public long countFavorites() {
        return favoriteRepository.count();
    }

    /** {@inheritDoc} */
    @Override
    public Favorite createFavorite(Restaurant restaurant, User user) {
        Favorite favorite = Favorite.builder()
                .restaurant(restaurant)
                .user(user)
                .build();
        return favoriteRepository.save(favorite);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteFavorite(Favorite favorite) {
        favoriteRepository.delete(favorite);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isFavorite(Restaurant restaurant, User user) {
        return favoriteRepository.findByRestaurantIdAndUserId(restaurant.getId(), user.getId()).isPresent();
    }
}
