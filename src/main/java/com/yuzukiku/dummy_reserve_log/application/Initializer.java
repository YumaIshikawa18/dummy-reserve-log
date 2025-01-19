package com.yuzukiku.dummy_reserve_log.application;

import com.yuzukiku.dummy_reserve_log.domain.entity.*;
import com.yuzukiku.dummy_reserve_log.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {

    private final CampRepository campRepository;
    private final FacilityRepository facilityRepository;
    private final CampFacilityRepository campFacilityRepository;
    private final RentalRepository rentalRepository;
    private final CampRentalRepository campRentalRepository;
    private final CustomerRepository customerRepository;
    private final CampService campService;

    /**
     * enumで設定した値をDBにinsertするため　data.sqlだとUUIDの生成を動的に出来なかったため
     */
    @Override
    public void run(String... args) {
        initializeFacilities();
        initializeRentals();
        initializeCamps();
        initializeCustomers();
        campService.updateAllCampLevels();
    }

    private void initializeFacilities() {
        for (FacilityType facilityType : FacilityType.values()) {
            Facilities facility = facilityType.getFacilities();

            boolean exists = facilityRepository.findAll().stream()
                    .anyMatch(f -> f.getName().equals(facility.getName()));

            if (!exists) {
                facilityRepository.save(facility);
            }
        }
    }

    private void initializeRentals() {
        for (RentalType rentalType : RentalType.values()) {
            Rentals rental = rentalType.getRentals();

            boolean exists = rentalRepository.findAll().stream()
                    .anyMatch(r -> r.getName().equals(rental.getName()));

            if (!exists) {
                rentalRepository.save(rental);
            }
        }
    }

    private void initializeCamps() {
        for (ExampleCamps exampleCamp : ExampleCamps.values()) {
            Camps camps = exampleCamp.getCamps();

            boolean exists = campRepository.findAll().stream()
                    .anyMatch(c -> c.getName().equals(camps.getName()));

            if (!exists) {
                campRepository.save(camps);
            }

            for (FacilityType facilityType : exampleCamp.getFacilities()) {
                Facilities facility = facilityType.getFacilities();

                boolean facilityLinked = campFacilityRepository.findAll().stream()
                        .anyMatch(cf -> cf.getCampId().equals(camps.getCampId()) &&
                                cf.getFacilityId().equals(facility.getFacilityId()));

                if (!facilityLinked) {
                    campFacilityRepository.save(new CampFacilities(camps.getCampId(), facility.getFacilityId()));
                }
            }

            for (RentalType rentalType : exampleCamp.getRentals()) {
                Rentals rental = rentalType.getRentals();

                boolean rentalLinked = campRentalRepository.findAll().stream()
                        .anyMatch(cr -> cr.getCampId().equals(camps.getCampId()) &&
                                cr.getRentalId().equals(rental.getRentalId()));

                if (!rentalLinked) {
                    campRentalRepository.save(new CampRentals(camps.getCampId(), rental.getRentalId()));
                }
            }
        }
    }

    private void initializeCustomers() {
        for (ExampleCustomers exampleCustomers : ExampleCustomers.values()) {
            Customers customers = exampleCustomers.getCustomers();

            boolean exists = customerRepository.findAll().stream()
                    .anyMatch(e -> e.getName().equals(customers.getName()));

            if (!exists) {
                customerRepository.save(customers);
            }
        }
    }
}
