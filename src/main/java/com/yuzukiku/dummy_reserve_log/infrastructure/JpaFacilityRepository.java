package com.yuzukiku.dummy_reserve_log.infrastructure;

import com.yuzukiku.dummy_reserve_log.domain.entity.Facilities;
import com.yuzukiku.dummy_reserve_log.domain.repository.FacilityRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaFacilityRepository extends FacilityRepository, JpaRepository<Facilities, UUID> {
}
