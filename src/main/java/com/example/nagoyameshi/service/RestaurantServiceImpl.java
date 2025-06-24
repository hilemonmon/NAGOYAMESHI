package com.example.nagoyameshi.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;

    @Override
    public List<Restaurant> getRestaurants(String name) {
        if (name == null || name.isBlank()) {
            return restaurantRepository.findAll();
        }
        return restaurantRepository.findByNameContaining(name);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findAllRestaurantsByOrderByCreatedAtDesc(Pageable pageable) {
        return restaurantRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findAllRestaurantsByOrderByLowestPriceAsc(Pageable pageable) {
        return restaurantRepository.findAllByOrderByLowestPriceAsc(pageable);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findRestaurantsByNameLikeOrAddressLikeOrCategoryNameLikeOrderByCreatedAtDesc(String keyword,
            Pageable pageable) {
        String like = "%" + keyword + "%";
        return restaurantRepository
                .findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByCreatedAtDesc(like, like, like, pageable);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findRestaurantsByNameLikeOrAddressLikeOrCategoryNameLikeOrderByLowestPriceAsc(String keyword,
            Pageable pageable) {
        String like = "%" + keyword + "%";
        return restaurantRepository
                .findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByLowestPriceAsc(like, like, like, pageable);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findRestaurantsByCategoryIdOrderByCreatedAtDesc(Integer categoryId, Pageable pageable) {
        return restaurantRepository.findByCategoryIdOrderByCreatedAtDesc(categoryId, pageable);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findRestaurantsByCategoryIdOrderByLowestPriceAsc(Integer categoryId, Pageable pageable) {
        return restaurantRepository.findByCategoryIdOrderByLowestPriceAsc(categoryId, pageable);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findRestaurantsByLowestPriceLessThanEqualOrderByCreatedAtDesc(Integer price,
            Pageable pageable) {
        return restaurantRepository.findByLowestPriceLessThanEqualOrderByCreatedAtDesc(price, pageable);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findRestaurantsByLowestPriceLessThanEqualOrderByLowestPriceAsc(Integer price,
            Pageable pageable) {
        return restaurantRepository.findByLowestPriceLessThanEqualOrderByLowestPriceAsc(price, pageable);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findAllRestaurantsByOrderByAverageScoreDesc(Pageable pageable) {
        return restaurantRepository.findAllByOrderByAverageScoreDesc(pageable);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findRestaurantsByNameLikeOrAddressLikeOrCategoryNameLikeOrderByAverageScoreDesc(
            String keyword, Pageable pageable) {
        String like = "%" + keyword + "%";
        return restaurantRepository.findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByAverageScoreDesc(
                like, like, like, pageable);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findRestaurantsByCategoryIdOrderByAverageScoreDesc(Integer categoryId, Pageable pageable) {
        return restaurantRepository.findByCategoryIdOrderByAverageScoreDesc(categoryId, pageable);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findRestaurantsByLowestPriceLessThanEqualOrderByAverageScoreDesc(Integer price,
            Pageable pageable) {
        return restaurantRepository.findByLowestPriceLessThanEqualOrderByAverageScoreDesc(price, pageable);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findAllRestaurantsOrderByReservationCountDesc(Pageable pageable) {
        return restaurantRepository.findAllOrderByReservationCountDesc(pageable);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findRestaurantsByNameLikeOrAddressLikeOrCategoryNameLikeOrderByReservationCountDesc(
            String keyword, Pageable pageable) {
        String like = "%" + keyword + "%";
        return restaurantRepository.findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByReservationCountDesc(
                like, like, like, pageable);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findRestaurantsByCategoryIdOrderByReservationCountDesc(Integer categoryId,
            Pageable pageable) {
        return restaurantRepository.findByCategoryIdOrderByReservationCountDesc(categoryId, pageable);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findRestaurantsByLowestPriceLessThanEqualOrderByReservationCountDesc(Integer price,
            Pageable pageable) {
        return restaurantRepository.findByLowestPriceLessThanEqualOrderByReservationCountDesc(price, pageable);
    }

    /** {@inheritDoc} */
    @Override
    public java.util.List<Integer> findRegularHolidayDayIndicesByRestaurantId(Long restaurantId) {
        return restaurantRepository.findRegularHolidayDayIndicesByRestaurantId(restaurantId);
    }

    /** {@inheritDoc} */
    @Override
    public java.util.Optional<Restaurant> findRestaurantById(Long id) {
        return restaurantRepository.findById(id);
    }
}
