package com.example.nagoyameshi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.nagoyameshi.entity.Restaurant;

@DataJpaTest
class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @DisplayName("findByNameContaining は名前で部分一致検索できる")
    void findByNameContaining_は名前で部分一致検索できる() {
        Restaurant r1 = Restaurant.builder().name("Nagoya Ramen").description("desc").build();
        Restaurant r2 = Restaurant.builder().name("Tokyo Ramen").description("desc").build();
        restaurantRepository.saveAll(List.of(r1, r2));

        List<Restaurant> result = restaurantRepository.findByNameContaining("Nagoya");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Nagoya Ramen");
    }

    @Test
    @DisplayName("findByHighestPriceLessThanEqualOrderByCreatedAtDesc は最高価格以下で検索できる")
    void findByHighestPriceLessThanEqualOrderByCreatedAtDesc_は最高価格以下で検索できる() {
        Restaurant r1 = Restaurant.builder().name("A").highestPrice(3000).build();
        Restaurant r2 = Restaurant.builder().name("B").highestPrice(4000).build();
        restaurantRepository.saveAll(List.of(r1, r2));

        List<Restaurant> result = restaurantRepository
                .findByHighestPriceLessThanEqualOrderByCreatedAtDesc(3500, org.springframework.data.domain.Pageable.unpaged())
                .getContent();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("A");
    }
}
