package com.yuzukiku.dummy_reserve_log.domain.repository;

import com.yuzukiku.dummy_reserve_log.domain.entity.Camps;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CampRepository {

    Optional<Camps> findByCampId(UUID campId);

    Camps save(Camps camps);

    List<Camps> findAll();
}