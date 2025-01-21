package com.yuzukiku.dummy_reserve_log.infrastruture;

import com.yuzukiku.dummy_reserve_log.domain.entity.CampRentalId;
import com.yuzukiku.dummy_reserve_log.domain.entity.CampRentals;
import com.yuzukiku.dummy_reserve_log.domain.repository.CampRentalRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCampRentalRepository extends CampRentalRepository, JpaRepository<CampRentals, CampRentalId> {
}
