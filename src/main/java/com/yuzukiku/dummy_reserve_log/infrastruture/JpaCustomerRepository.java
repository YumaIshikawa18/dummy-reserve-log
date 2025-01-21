package com.yuzukiku.dummy_reserve_log.infrastruture;

import com.yuzukiku.dummy_reserve_log.domain.entity.Customers;
import com.yuzukiku.dummy_reserve_log.domain.repository.CustomerRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaCustomerRepository extends CustomerRepository, JpaRepository<Customers, UUID> {
}
