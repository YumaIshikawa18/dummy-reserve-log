package com.yuzukiku.dummy_reserve_log.infrastruture;

import com.yuzukiku.dummy_reserve_log.domain.entity.Rentals;
import com.yuzukiku.dummy_reserve_log.domain.repository.RentalRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaRentalRepository extends RentalRepository, JpaRepository<Rentals, UUID> {
}
