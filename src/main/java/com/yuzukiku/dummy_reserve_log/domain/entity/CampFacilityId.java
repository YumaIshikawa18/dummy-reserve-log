package com.yuzukiku.dummy_reserve_log.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampFacilityId implements Serializable {

    private UUID campId;
    private UUID facilityId;
}