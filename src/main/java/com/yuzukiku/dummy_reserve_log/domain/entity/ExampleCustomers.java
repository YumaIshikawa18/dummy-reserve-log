package com.yuzukiku.dummy_reserve_log.domain.entity;

import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

/**
 * Duffy and Friends
 */
@Getter
public enum ExampleCustomers {

    DUFFY(new Customers(UUID.randomUUID(), "Duffy", "duffy@disney.com", hashPassword("bearfriend"))),
    SHELLIE_MAY(new Customers(UUID.randomUUID(), "ShellieMay", "shelliemay@disney.com", hashPassword("kindheart"))),
    GELATONI(new Customers(UUID.randomUUID(), "Gelatoni", "gelatoni@disney.com", hashPassword("artistcat"))),
    STELLA_LOU(new Customers(UUID.randomUUID(), "StellaLou", "stellalou@disney.com", hashPassword("dreambunny"))),
    COOKIE_ANN(new Customers(UUID.randomUUID(), "CookieAnn", "cookieann@disney.com", hashPassword("bakerdog"))),
    OLUMEL(new Customers(UUID.randomUUID(), "‘Olu Mel", "olumel@disney.com", hashPassword("ukuleleturtle"))),
    LINABELL(new Customers(UUID.randomUUID(), "LinaBell", "linabell@disney.com", hashPassword("foxdetective")));

    private final Customers customers;

    ExampleCustomers(Customers customers) {
        this.customers = customers;
    }

    private static String hashPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}