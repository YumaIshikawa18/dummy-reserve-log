package com.yuzukiku.dummy_reserve_log.application;

import com.yuzukiku.dummy_reserve_log.domain.entity.Customers;
import com.yuzukiku.dummy_reserve_log.domain.entity.GameCustomers;
import com.yuzukiku.dummy_reserve_log.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final CampService campService;

    @Override
    public void run(String... args) {
        for (GameCustomers gameCustomers : GameCustomers.values()) {
            Customers customers = gameCustomers.getCustomers();

            boolean exists = customerRepository.findAll().stream()
                    .anyMatch(e -> e.getName().equals(customers.getName()));

            if (!exists) {
                UUID newId = UUID.randomUUID();
                String hashedPassword = passwordEncoder.encode(customers.getPassword());
                Customers entity = new Customers(
                        newId,
                        customers.getName(),
                        customers.getAddress(),
                        hashedPassword
                );
                customerRepository.save(entity);
            }
        }
        campService.updateAllCampLevels();
    }
}
