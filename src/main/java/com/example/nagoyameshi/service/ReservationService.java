package com.example.nagoyameshi.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationRegisterForm;

/**
 * 予約情報を扱うサービスインターフェース。
 */
public interface ReservationService {

    /** ID を指定して予約を取得する。 */
    Optional<Reservation> findReservationById(Long id);

    /** 指定ユーザーの予約を予約日時降順で取得する。 */
    Page<Reservation> findReservationsByUserOrderByReservedDatetimeDesc(User user, Pageable pageable);

    /** 予約件数を数える。 */
    long countReservations();

    /** 最新の予約を1件取得する。 */
    Optional<Reservation> findFirstReservationByOrderByIdDesc();

    /** 予約を新規登録する。 */
    Reservation createReservation(ReservationRegisterForm form, Restaurant restaurant, User user);

    /** 予約を削除する。 */
    void deleteReservation(Reservation reservation);

    /**
     * 指定日時が現在より2時間以上未来か判定する。
     *
     * @param target 対象日時
     * @return 2時間以上未来なら true
     */
    boolean isAtLeastTwoHoursInFuture(LocalDateTime target);
}
