package com.yuzukiku.dummy_reserve_log.domain.entity;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

/**
 * Fiction
 */
@Getter
public enum ExampleCamps {

    FUJI(new Camps(UUID.randomUUID(), "富士山キャンプ場", null, "静岡県富士宮市",
            "https://maps.google.com/?q=35.3606,138.7274", 5000, 10,
            "富士山の絶景を楽しめる", null),
            List.of(FacilityType.KITCHEN, FacilityType.HOT_WATER, FacilityType.AC_POWER,
                    FacilityType.SHOP, FacilityType.SHOWER, FacilityType.TOILET),
            List.of(RentalType.TENT, RentalType.SLEEPING_BAG)),
    BIWA(new Camps(UUID.randomUUID(), "琵琶湖キャンプ場", null, "滋賀県大津市",
            "https://maps.google.com/?q=35.2010,135.9847", 4500, 15,
            "琵琶湖の湖畔にある", null),
            List.of(FacilityType.SAUNA, FacilityType.SHOP, FacilityType.TOILET, FacilityType.PLAYGROUND),
            List.of(RentalType.TENT, RentalType.LANTERN));

    private final Camps camps;
    private final List<FacilityType> facilities;
    private final List<RentalType> rentals;

    ExampleCamps(Camps camps, List<FacilityType> facilities, List<RentalType> rentals) {
        this.camps = camps;
        this.facilities = facilities;
        this.rentals = rentals;
    }
}
