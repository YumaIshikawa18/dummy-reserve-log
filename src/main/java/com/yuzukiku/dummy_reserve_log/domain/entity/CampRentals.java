package com.yuzukiku.dummy_reserve_log.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@IdClass(CampRentalId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampRentals {

    @Id
    private UUID campId;
    @Id
    private UUID rentalId;
}