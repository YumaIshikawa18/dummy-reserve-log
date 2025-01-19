package com.yuzukiku.dummy_reserve_log.infrastruture;

import com.yuzukiku.dummy_reserve_log.domain.entity.CampFacilities;
import com.yuzukiku.dummy_reserve_log.domain.entity.CampFacilityId;
import com.yuzukiku.dummy_reserve_log.domain.repository.CampFacilityRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaCampFacilityRepository extends CampFacilityRepository, JpaRepository<CampFacilities, CampFacilityId> {

    List<CampFacilities> findByCampId(UUID campId);
}
