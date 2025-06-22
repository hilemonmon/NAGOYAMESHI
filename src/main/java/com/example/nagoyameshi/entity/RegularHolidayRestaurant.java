package com.example.nagoyameshi.entity;

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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.example.nagoyameshi.entity.base.BaseTimestampEntity;

/**
 * 店舗と定休日を結び付ける中間エンティティ。
 * 追加・削除の履歴を把握できるようタイムスタンプを持つ。
 */
@Entity
@Table(name = "regular_holiday_restaurant")
@Data
@EqualsAndHashCode(callSuper = false) // タイムスタンプは等価判定に含めない
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegularHolidayRestaurant extends BaseTimestampEntity {
    /** 主キー。自動採番 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 紐付く店舗 */
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    /** 紐付く定休日 */
    @ManyToOne
    @JoinColumn(name = "regular_holiday_id")
    private RegularHoliday regularHoliday;
}
