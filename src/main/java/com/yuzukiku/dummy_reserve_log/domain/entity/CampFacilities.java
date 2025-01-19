package com.yuzukiku.dummy_reserve_log.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Entity
@IdClass(CampFacilityId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampFacilities {

    @Id
    private UUID campId;

    @Id
    private UUID facilityId;
}
