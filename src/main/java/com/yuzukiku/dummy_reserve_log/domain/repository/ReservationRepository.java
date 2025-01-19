package com.yuzukiku.dummy_reserve_log.domain.repository;

import com.yuzukiku.dummy_reserve_log.domain.entity.Reservations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {

    Optional<Reservations> findByReservationId(UUID reservationId);

    Reservations save(Reservations reservations);


    List<Reservations> findAll();

    List<Reservations> findByCustomerId(UUID customerId);

    List<Reservations> findByCampId(UUID campId);
}
