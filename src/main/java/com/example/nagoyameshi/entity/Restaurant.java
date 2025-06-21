package com.example.nagoyameshi.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import com.example.nagoyameshi.entity.CategoryRestaurant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restaurants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String image;

    private String description;

    private Integer lowestPrice;

    private Integer highestPrice;

    private String postalCode;

    private String address;

    private LocalTime openingTime;

    private LocalTime closingTime;

    private Integer seatingCapacity;

    // 店舗に紐づくカテゴリ情報。追加順で取得できるようid昇順で並び替える
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<CategoryRestaurant> categoriesRestaurants;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
