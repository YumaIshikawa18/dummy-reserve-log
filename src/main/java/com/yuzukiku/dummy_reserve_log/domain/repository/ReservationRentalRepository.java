package com.yuzukiku.dummy_reserve_log.domain.repository;

import com.yuzukiku.dummy_reserve_log.domain.entity.ReservationRentals;

import java.util.List;
import java.util.UUID;

public interface ReservationRentalRepository {

    List<ReservationRentals> findByReservationId(UUID reservationId);

    ReservationRentals save(ReservationRentals reservationRentals);
}