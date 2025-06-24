package com.example.nagoyameshi.entity;

import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.example.nagoyameshi.entity.base.BaseTimeEntity;

@Entity
@Table(name = "restaurants")
@Data
@EqualsAndHashCode(callSuper = false) // タイムスタンプは等価判定に含めない
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant extends BaseTimeEntity {
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

    // 店舗に紐づく定休日情報。追加順で取得できるようid昇順で並び替える
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<RegularHolidayRestaurant> regularHolidaysRestaurants;

    // 店舗に紐づくレビュー。店舗削除時にあわせて削除される

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<Review> reviews;

    /**
     * レビュー平均点を計算して返却する。レビューが無い場合は0.0。
     *
     * @return 平均スコア
     */
    @jakarta.persistence.Transient
    public Double getAverageScore() {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream().mapToInt(Review::getScore).average().orElse(0.0);
    }

}
