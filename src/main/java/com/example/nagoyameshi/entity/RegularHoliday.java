package com.example.nagoyameshi.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 定休日マスタを表すエンティティ。
 */
@Entity
@Table(name = "regular_holidays")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegularHoliday {
    /** 主キー。自動採番 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 曜日名や"不定休"などの表示文字列 */
    @jakarta.persistence.Column(name = "\"day\"")
    private String day;

    /** 曜日の並び替え順。NULL許容 */
    private Integer dayIndex;

    /** この定休日を持つ店舗との紐付け一覧 */
    @OneToMany(mappedBy = "regularHoliday", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegularHolidayRestaurant> regularHolidaysRestaurants;
}
