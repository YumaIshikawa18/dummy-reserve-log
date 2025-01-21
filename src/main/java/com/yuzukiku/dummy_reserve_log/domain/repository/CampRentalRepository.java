package com.yuzukiku.dummy_reserve_log.domain.repository;



import com.yuzukiku.dummy_reserve_log.domain.entity.CampRentals;

import java.util.List;
import java.util.UUID;

public interface CampRentalRepository {
    List<CampRentals> findByCampId(UUID campId);

    List<CampRentals> findAll();

    CampRentals save(CampRentals campRentals);
}
