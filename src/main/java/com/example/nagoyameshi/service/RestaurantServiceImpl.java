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
}
