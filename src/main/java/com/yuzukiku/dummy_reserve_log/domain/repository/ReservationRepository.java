package com.yuzukiku.dummy_reserve_log.domain.repository;

import com.yuzukiku.dummy_reserve_log.domain.entity.Reservations;

import java.util.List;
import java.util.UUID;

public interface ReservationRepository {

    Reservations save(Reservations reservations);

    List<Reservations> findAll();

    List<Reservations> findByCampId(UUID campId);
}