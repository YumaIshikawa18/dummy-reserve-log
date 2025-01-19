package com.yuzukiku.dummy_reserve_log.infrastruture;

import com.yuzukiku.dummy_reserve_log.domain.entity.Reservations;
import com.yuzukiku.dummy_reserve_log.domain.repository.ReservationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaReservationRepository extends ReservationRepository, JpaRepository<Reservations, UUID> {
}
