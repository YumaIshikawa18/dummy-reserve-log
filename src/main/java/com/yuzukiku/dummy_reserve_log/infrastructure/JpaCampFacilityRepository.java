package com.yuzukiku.dummy_reserve_log.infrastructure;

import com.yuzukiku.dummy_reserve_log.domain.entity.CampFacilities;
import com.yuzukiku.dummy_reserve_log.domain.entity.CampFacilityId;
import com.yuzukiku.dummy_reserve_log.domain.repository.CampFacilityRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaCampFacilityRepository extends CampFacilityRepository, JpaRepository<CampFacilities, CampFacilityId> {

    List<CampFacilities> findByCampId(UUID campId);
}
