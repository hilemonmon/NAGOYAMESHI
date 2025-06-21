package com.example.nagoyameshi.service;

import java.util.List;
import java.util.Optional;

import com.example.nagoyameshi.entity.RegularHoliday;

/**
 * 定休日マスタを扱うサービスインターフェース。
 */
public interface RegularHolidayService {

    /** IDを指定して定休日を取得する。 */
    Optional<RegularHoliday> findRegularHolidayById(Integer id);

    /** 全ての定休日を取得する。 */
    List<RegularHoliday> findAllRegularHolidays();
}
