package com.yuzukiku.dummy_reserve_log.domain.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public enum RentalType {

    TENT(new Rentals(UUID.randomUUID(), "テント", 1000)),
    SLEEPING_BAG(new Rentals(UUID.randomUUID(), "寝袋", 500)),
    CAMP_CHAIR(new Rentals(UUID.randomUUID(), "キャンプチェア", 300)),
    LANTERN(new Rentals(UUID.randomUUID(), "ランタン", 200));

    private final Rentals rentals;

    RentalType(Rentals rentals) {
        this.rentals = rentals;
    }
}