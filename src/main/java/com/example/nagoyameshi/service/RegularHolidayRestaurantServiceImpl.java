package com.example.nagoyameshi.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.RegularHoliday;
import com.example.nagoyameshi.entity.RegularHolidayRestaurant;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.repository.RegularHolidayRepository;
import com.example.nagoyameshi.repository.RegularHolidayRestaurantRepository;

import lombok.RequiredArgsConstructor;

/**
 * {@link RegularHolidayRestaurantService} の実装クラス。
 */
@Service
@RequiredArgsConstructor
public class RegularHolidayRestaurantServiceImpl implements RegularHolidayRestaurantService {

    private final RegularHolidayRestaurantRepository regularHolidayRestaurantRepository;
    private final RegularHolidayRepository regularHolidayRepository;

    @Override
    public List<Integer> findRegularHolidayIdsByRestaurant(Restaurant restaurant) {
        return regularHolidayRestaurantRepository.findRegularHolidayIdsByRestaurant(restaurant);
    }

    @Override
    public void createRegularHolidaysRestaurants(List<Integer> regularHolidayIds, Restaurant restaurant) {
        if (regularHolidayIds == null) {
            return;
        }
        for (Integer holidayId : regularHolidayIds) {
            if (holidayId == null) {
                continue;
            }
            RegularHoliday holiday = regularHolidayRepository.findById(holidayId).orElseThrow();
            // 既に紐付いていないか確認
            RegularHolidayRestaurant existing = regularHolidayRestaurantRepository.findByRestaurantAndRegularHoliday(restaurant, holiday);
            if (Objects.isNull(existing)) {
                RegularHolidayRestaurant rr = RegularHolidayRestaurant.builder()
                        .restaurant(restaurant)
                        .regularHoliday(holiday)
                        .build();
                regularHolidayRestaurantRepository.save(rr);
            }
        }
    }

    @Override
    public void syncRegularHolidaysRestaurants(List<Integer> regularHolidayIds, Restaurant restaurant) {
        List<RegularHolidayRestaurant> current = regularHolidayRestaurantRepository.findByRestaurantOrderByIdAsc(restaurant);
        for (RegularHolidayRestaurant rr : current) {
            Integer id = rr.getRegularHoliday().getId();
            if (regularHolidayIds == null || !regularHolidayIds.contains(id)) {
                regularHolidayRestaurantRepository.delete(rr);
            }
        }
        createRegularHolidaysRestaurants(regularHolidayIds, restaurant);
    }
}
