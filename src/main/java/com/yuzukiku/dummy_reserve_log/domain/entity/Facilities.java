package com.yuzukiku.dummy_reserve_log.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Facilities {

    @Id
    private UUID facilityId;
    private String name;
    private int points;
}