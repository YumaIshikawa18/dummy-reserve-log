package com.yuzukiku.dummy_reserve_log.infrastruture;

import com.yuzukiku.dummy_reserve_log.domain.entity.Camps;
import com.yuzukiku.dummy_reserve_log.domain.repository.CampRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaCampRepository extends CampRepository, JpaRepository<Camps, UUID> {
}
