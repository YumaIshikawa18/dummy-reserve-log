package com.yuzukiku.dummy_reserve_log.domain.repository;


import com.yuzukiku.dummy_reserve_log.domain.entity.CampFacilities;

import java.util.List;
import java.util.UUID;

public interface CampFacilityRepository {
    List<CampFacilities> findByCampId(UUID campId);
}
