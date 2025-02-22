package com.yuzukiku.dummy_reserve_log.domain.repository;

import com.yuzukiku.dummy_reserve_log.domain.entity.Facilities;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FacilityRepository {

    Optional<Facilities> findByFacilityId(UUID facilityId);

    Facilities save(Facilities facilities);

    List<Facilities> findAll();
}