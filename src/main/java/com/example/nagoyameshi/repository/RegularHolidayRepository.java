package com.example.nagoyameshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.RegularHoliday;

/**
 * 定休日情報にアクセスするリポジトリ。
 */
public interface RegularHolidayRepository extends JpaRepository<RegularHoliday, Integer> {
}
