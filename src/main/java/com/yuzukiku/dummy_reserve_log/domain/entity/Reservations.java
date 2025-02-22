package com.yuzukiku.dummy_reserve_log.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservations {

    @Id
    private UUID reservationId;
    private UUID campId;
    private UUID customerId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    @Enumerated(EnumType.STRING)
    private status status;
    private Integer numberOfTents;
    private Integer totalPrice;
}