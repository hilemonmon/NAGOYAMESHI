package com.example.nagoyameshi.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.nagoyameshi.entity.base.BaseTimestampEntity;

@Entity
@Table(name = "category_restaurant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRestaurant extends BaseTimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 主キー。自動採番される
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id") // restaurants テーブルの主キーを参照
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "category_id") // categories テーブルの主キーを参照
    private Category category;

}
