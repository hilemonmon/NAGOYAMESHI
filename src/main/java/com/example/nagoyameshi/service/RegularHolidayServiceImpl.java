package com.example.nagoyameshi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.RegularHoliday;
import com.example.nagoyameshi.repository.RegularHolidayRepository;

import lombok.RequiredArgsConstructor;

/**
 * {@link RegularHolidayService} の実装クラス。
 */
@Service
@RequiredArgsConstructor
public class RegularHolidayServiceImpl implements RegularHolidayService {

    private final RegularHolidayRepository regularHolidayRepository;

    /** {@inheritDoc} */
    @Override
    public Optional<RegularHoliday> findRegularHolidayById(Integer id) {
        return regularHolidayRepository.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public List<RegularHoliday> findAllRegularHolidays() {
        return regularHolidayRepository.findAll();
    }
}
