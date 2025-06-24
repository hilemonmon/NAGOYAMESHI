package com.example.nagoyameshi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.User;

import com.example.nagoyameshi.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * 指定ユーザーに紐づく予約を予約日時の降順で取得する。
     *
     * @param user     予約したユーザー
     * @param pageable ページ情報
     * @return 予約ページ
     */
    Page<Reservation> findByUserOrderByReservedDatetimeDesc(User user, Pageable pageable);

    /**
     * 最新の予約をID降順で1件取得する。
     *
     * @return 最新の予約
     */
    java.util.Optional<Reservation> findFirstByOrderByIdDesc();
}
