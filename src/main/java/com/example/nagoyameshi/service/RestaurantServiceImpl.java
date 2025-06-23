package com.example.nagoyameshi.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

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
        // pageable が持つソート情報は使用しないため、ページ番号とサイズのみ再指定
        Pageable fixed = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return restaurantRepository.findAllByOrderByCreatedAtDesc(fixed);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findAllRestaurantsByOrderByLowestPriceAsc(Pageable pageable) {
        Pageable fixed = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return restaurantRepository.findAllByOrderByLowestPriceAsc(fixed);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findRestaurantsByNameLikeOrAddressLikeOrCategoryNameLikeOrderByCreatedAtDesc(String keyword,
            Pageable pageable) {
        String like = "%" + keyword + "%";
        Pageable fixed = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return restaurantRepository
                .findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByCreatedAtDesc(like, like, like, fixed);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findRestaurantsByNameLikeOrAddressLikeOrCategoryNameLikeOrderByLowestPriceAsc(String keyword,
            Pageable pageable) {
        String like = "%" + keyword + "%";
        Pageable fixed = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return restaurantRepository
                .findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByLowestPriceAsc(like, like, like, fixed);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findRestaurantsByCategoryIdOrderByCreatedAtDesc(Integer categoryId, Pageable pageable) {
        Pageable fixed = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return restaurantRepository.findByCategoryIdOrderByCreatedAtDesc(categoryId, fixed);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findRestaurantsByCategoryIdOrderByLowestPriceAsc(Integer categoryId, Pageable pageable) {
        Pageable fixed = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return restaurantRepository.findByCategoryIdOrderByLowestPriceAsc(categoryId, fixed);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findRestaurantsByLowestPriceLessThanEqualOrderByCreatedAtDesc(Integer price,
            Pageable pageable) {
        Pageable fixed = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return restaurantRepository.findByLowestPriceLessThanEqualOrderByCreatedAtDesc(price, fixed);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Restaurant> findRestaurantsByLowestPriceLessThanEqualOrderByLowestPriceAsc(Integer price,
            Pageable pageable) {
        Pageable fixed = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return restaurantRepository.findByLowestPriceLessThanEqualOrderByLowestPriceAsc(price, fixed);
    }
}
