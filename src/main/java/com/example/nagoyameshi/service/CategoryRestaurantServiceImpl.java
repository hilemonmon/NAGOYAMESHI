package com.example.nagoyameshi.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.CategoryRestaurant;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.repository.CategoryRestaurantRepository;

import lombok.RequiredArgsConstructor;

/**
 * {@link CategoryRestaurantService} の実装クラス。
 */
@Service
@RequiredArgsConstructor
public class CategoryRestaurantServiceImpl implements CategoryRestaurantService {

    private final CategoryRestaurantRepository categoryRestaurantRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<Integer> findCategoryIdsByRestaurantOrderByIdAsc(Restaurant restaurant) {
        return categoryRestaurantRepository.findCategoryIdsByRestaurantOrderByIdAsc(restaurant);
    }

    @Override
    public void createCategoriesRestaurants(List<Integer> categoryIds, Restaurant restaurant) {
        if (categoryIds == null) {
            return;
        }
        for (Integer categoryId : categoryIds) {
            if (categoryId == null) {
                continue;
            }
            Category category = categoryRepository.findById(Long.valueOf(categoryId))
                    .orElseThrow();
            // 重複していないか確認
            CategoryRestaurant existing = categoryRestaurantRepository.findByRestaurantAndCategory(restaurant, category);
            if (Objects.isNull(existing)) {
                CategoryRestaurant cr = CategoryRestaurant.builder()
                        .restaurant(restaurant)
                        .category(category)
                        .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                        .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
                        .build();
                categoryRestaurantRepository.save(cr);
            }
        }
    }

    @Override
    public void syncCategoriesRestaurants(List<Integer> categoryIds, Restaurant restaurant) {
        List<CategoryRestaurant> current = categoryRestaurantRepository.findByRestaurantOrderByIdAsc(restaurant);
        // 削除対象を判定
        for (CategoryRestaurant cr : current) {
            Integer id = Math.toIntExact(cr.getCategory().getId());
            if (categoryIds == null || !categoryIds.contains(id)) {
                categoryRestaurantRepository.delete(cr);
            }
        }
        // 追加すべきカテゴリを登録
        createCategoriesRestaurants(categoryIds, restaurant);
    }
}
