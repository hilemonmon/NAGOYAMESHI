package com.example.nagoyameshi.service;

import java.util.List;

import com.example.nagoyameshi.entity.Restaurant;

public interface RestaurantService {
    List<Restaurant> getRestaurants(String name);
}
