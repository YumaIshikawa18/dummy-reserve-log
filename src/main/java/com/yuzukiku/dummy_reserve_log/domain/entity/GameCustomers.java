package com.yuzukiku.dummy_reserve_log.domain.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public enum GameCustomers {
    LAZ(new Customers(UUID.randomUUID(), "ZETA Laz", "laz@zeta.com", "chamber")),
    CROW(new Customers(UUID.randomUUID(), "ZETA crow", "crow@zeta.com", "breech")),
    TENNN(new Customers(UUID.randomUUID(), "ZETA TENNN", "tennn@zeta.com", "raze")),
    DEP(new Customers(UUID.randomUUID(), "ZETA DEP", "dep@zeta.com", "neon")),
    SUGARZ3RO(new Customers(UUID.randomUUID(), "ZETA SugarZ3ro", "sugarz3ro@zeta.com", "astra"));

    private final Customers customers;

    GameCustomers(Customers customers) {
        this.customers = customers;
    }

}
