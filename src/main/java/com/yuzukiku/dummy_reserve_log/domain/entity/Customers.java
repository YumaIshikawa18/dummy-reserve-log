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
public class Customers {

    @Id
    private UUID customerId;
    private String name;
    private String address;
    private String password;
}