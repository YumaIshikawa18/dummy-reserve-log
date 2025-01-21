package com.yuzukiku.dummy_reserve_log.infrastruture;

import com.yuzukiku.dummy_reserve_log.domain.entity.ReservationRentalId;
import com.yuzukiku.dummy_reserve_log.domain.entity.ReservationRentals;
import com.yuzukiku.dummy_reserve_log.domain.repository.ReservationRentalRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaReservationRentalRepository extends ReservationRentalRepository, JpaRepository<ReservationRentals, ReservationRentalId> {
}
