package com.example.nagoyameshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.CategoryRestaurant;

public interface CategoryRestaurantRepository extends JpaRepository<CategoryRestaurant, Long> {
}
