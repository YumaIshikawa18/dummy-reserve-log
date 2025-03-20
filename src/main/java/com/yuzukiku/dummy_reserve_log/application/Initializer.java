package com.yuzukiku.dummy_reserve_log.application;

import com.yuzukiku.dummy_reserve_log.domain.entity.*;
import com.yuzukiku.dummy_reserve_log.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

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
     * enumで設定した値をDBにinsertするための初期化処理
     * data.sqlではUUIDの動的生成が困難なため、本処理を用いて初期化する
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
        List<Facilities> existingFacilities = facilityRepository.findAll();
        for (FacilityType facilityType : FacilityType.values()) {
            Facilities facility = facilityType.getFacilities();
            boolean exists = existingFacilities.stream()
                    .anyMatch(f -> f.getName().equals(facility.getName()));
            if (!exists) {
                facilityRepository.save(facility);
                existingFacilities.add(facility); // ローカルキャッシュを更新
            }
        }
    }

    private void initializeRentals() {
        List<Rentals> existingRentals = rentalRepository.findAll();
        for (RentalType rentalType : RentalType.values()) {
            Rentals rental = rentalType.getRentals();
            boolean exists = existingRentals.stream()
                    .anyMatch(r -> r.getName().equals(rental.getName()));
            if (!exists) {
                rentalRepository.save(rental);
                existingRentals.add(rental); // ローカルキャッシュを更新
            }
        }
    }

    private void initializeCamps() {
        List<Camps> existingCamps = campRepository.findAll();
        List<CampFacilities> existingCampFacilities = campFacilityRepository.findAll();
        List<CampRentals> existingCampRentals = campRentalRepository.findAll();

        for (ExampleCamps exampleCamp : ExampleCamps.values()) {
            Camps camp = exampleCamp.getCamps();
            boolean exists = existingCamps.stream()
                    .anyMatch(c -> c.getName().equals(camp.getName()));
            if (!exists) {
                campRepository.save(camp);
                existingCamps.add(camp);
            }
            // 施設とのリンク処理
            for (FacilityType facilityType : exampleCamp.getFacilities()) {
                Facilities facility = facilityType.getFacilities();
                boolean facilityLinked = existingCampFacilities.stream()
                        .anyMatch(cf -> cf.getCampId().equals(camp.getCampId()) &&
                                cf.getFacilityId().equals(facility.getFacilityId()));
                if (!facilityLinked) {
                    CampFacilities link = new CampFacilities(camp.getCampId(), facility.getFacilityId());
                    campFacilityRepository.save(link);
                    existingCampFacilities.add(link);
                }
            }
            // レンタルとのリンク処理
            for (RentalType rentalType : exampleCamp.getRentals()) {
                Rentals rental = rentalType.getRentals();
                boolean rentalLinked = existingCampRentals.stream()
                        .anyMatch(cr -> cr.getCampId().equals(camp.getCampId()) &&
                                cr.getRentalId().equals(rental.getRentalId()));
                if (!rentalLinked) {
                    CampRentals link = new CampRentals(camp.getCampId(), rental.getRentalId());
                    campRentalRepository.save(link);
                    existingCampRentals.add(link);
                }
            }
        }
    }

    private void initializeCustomers() {
        List<Customers> existingCustomers = customerRepository.findAll();
        for (ExampleCustomers exampleCustomers : ExampleCustomers.values()) {
            Customers customer = exampleCustomers.getCustomers();
            boolean exists = existingCustomers.stream()
                    .anyMatch(c -> c.getName().equals(customer.getName()));
            if (!exists) {
                customerRepository.save(customer);
                existingCustomers.add(customer);
            }
        }
    }
}
