package com.yuzukiku.dummy_reserve_log.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rentals {

    @Id
    private UUID rentalId;
    private String name;
    private Integer pricePerDay;
}