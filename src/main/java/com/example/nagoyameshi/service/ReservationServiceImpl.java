package com.example.nagoyameshi.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationRegisterForm;
import com.example.nagoyameshi.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

/**
 * {@link ReservationService} の実装クラス。
 */
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    /** {@inheritDoc} */
    @Override
    public Optional<Reservation> findReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Reservation> findReservationsByUserOrderByReservedDatetimeDesc(User user, Pageable pageable) {
        return reservationRepository.findByUserOrderByReservedDatetimeDesc(user, pageable);
    }

    /** {@inheritDoc} */
    @Override
    public long countReservations() {
        return reservationRepository.count();
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Reservation> findFirstReservationByOrderByIdDesc() {
        return reservationRepository.findFirstByOrderByIdDesc();
    }

    /** {@inheritDoc} */
    @Override
    public Reservation createReservation(ReservationRegisterForm form, Restaurant restaurant, User user) {
        LocalDateTime reservedDateTime = LocalDateTime.of(form.getReservationDate(), form.getReservationTime());
        Reservation reservation = Reservation.builder()
                .reservedDatetime(reservedDateTime)
                .numberOfPeople(form.getNumberOfPeople())
                .restaurant(restaurant)
                .user(user)
                .build();
        return reservationRepository.save(reservation);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteReservation(Reservation reservation) {
        reservationRepository.delete(reservation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isAtLeastTwoHoursInFuture(LocalDateTime target) {
        return target.isAfter(LocalDateTime.now().plusHours(2));
    }
}
