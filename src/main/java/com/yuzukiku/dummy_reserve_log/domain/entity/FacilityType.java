package com.yuzukiku.dummy_reserve_log.domain.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public enum FacilityType {

    SAUNA(new Facilities(UUID.randomUUID(), "サウナ", 0)),
    KITCHEN(new Facilities(UUID.randomUUID(), "キッチン", 3)),
    HOT_WATER(new Facilities(UUID.randomUUID(), "給湯", 1)),
    AC_POWER(new Facilities(UUID.randomUUID(), "AC電源", 3)),
    SHOP(new Facilities(UUID.randomUUID(), "売店", 3)),
    SHOWER(new Facilities(UUID.randomUUID(), "シャワー", 3)),
    TOILET(new Facilities(UUID.randomUUID(), "水洗トイレ", 10)),
    PLAYGROUND(new Facilities(UUID.randomUUID(), "遊具", 1)),
    LAUNDRY(new Facilities(UUID.randomUUID(), "コインランドリー", 2)),
    FREE_WIFI(new Facilities(UUID.randomUUID(), "FREEWifi", 1));

    private final Facilities facilities;

    FacilityType(Facilities facilities) {
        this.facilities = facilities;
    }
}
