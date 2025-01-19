package com.yuzukiku.dummy_reserve_log.domain.repository;

import com.yuzukiku.dummy_reserve_log.domain.entity.Rentals;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentalRepository {

    Optional<Rentals> findByRentalId(UUID rentalId);

    Rentals save(Rentals rentals);

    List<Rentals> findAll();

}
