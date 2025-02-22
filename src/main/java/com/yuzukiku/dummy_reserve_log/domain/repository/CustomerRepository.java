package com.yuzukiku.dummy_reserve_log.domain.repository;

import com.yuzukiku.dummy_reserve_log.domain.entity.Customers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    Optional<Customers> findByCustomerId(UUID customerId);

    Customers save(Customers customers);

    List<Customers> findAll();
}