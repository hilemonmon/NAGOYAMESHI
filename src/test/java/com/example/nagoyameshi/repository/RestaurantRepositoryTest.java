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
    @DisplayName("findByNameContaining returns matching restaurants")
    void testFindByNameContaining() {
        Restaurant r1 = Restaurant.builder().name("Nagoya Ramen").description("desc").build();
        Restaurant r2 = Restaurant.builder().name("Tokyo Ramen").description("desc").build();
        restaurantRepository.saveAll(List.of(r1, r2));

        List<Restaurant> result = restaurantRepository.findByNameContaining("Nagoya");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Nagoya Ramen");
    }
}
