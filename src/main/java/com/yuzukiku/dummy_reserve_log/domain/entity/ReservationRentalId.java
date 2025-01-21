package com.yuzukiku.dummy_reserve_log.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRentalId implements Serializable {
    private UUID reservationId;
    private UUID rentalId;
}
