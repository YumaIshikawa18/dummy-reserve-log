package com.yuzukiku.dummy_reserve_log.application;

import com.yuzukiku.dummy_reserve_log.config.TimeConfig;
import com.yuzukiku.dummy_reserve_log.domain.entity.*;
import com.yuzukiku.dummy_reserve_log.domain.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;
    private final CampRepository campRepository;
    private final CustomerRepository customerRepository;
    private final RentalRepository rentalRepository;
    private final CampRentalRepository campRentalRepository;
    private final ReservationRentalRepository reservationRentalRepository;


    private final SecureRandom random = new SecureRandom();

    /**
     * 1999-02-11T00:00:00Z を基準とし、UTC, 8640倍
     * (TimeConfig は Clock を継承している)
     */
    private final TimeConfig timeConfig = new TimeConfig(
            Instant.parse("1999-02-11T00:00:00Z"),
            ZoneId.of("UTC"),
            8640
    );

    public List<ReservationsDTO> searchList() {
        List<Reservations> allReservations = reservationRepository.findAll();

        List<ReservationsDTO> reservationsDTOList = new ArrayList<>();
        for (Reservations reservation : allReservations) {

            Camps camps = campRepository.findByCampId(reservation.getCampId())
                    .orElse(null);

            Customers customers = customerRepository.findByCustomerId(reservation.getCustomerId())
                    .orElse(null);

            if (camps == null || customers == null) {
                logger.warn("Camp or Customer not found for reservation ID: {}", reservation.getReservationId());
                continue;
            }

            List<ReservationRentals> reservationRentals = reservationRentalRepository.findByReservationId(reservation.getReservationId());

            List<Rentals> rentals = new ArrayList<>();
            List<Integer> quantities = new ArrayList<>();

            for (ReservationRentals rr : reservationRentals) {
                rentalRepository.findByRentalId(rr.getRentalId())
                        .ifPresent(rentals::add);
                quantities.add(rr.getQuantity());
            }

            int calculatedTotalPrice = calculateTotalPrice(reservation, camps, rentals, quantities);

            ReservationsDTO reservationsDTO = new ReservationsDTO().buildDto(
                    reservation,
                    camps,
                    customers,
                    rentals,
                    quantities,
                    calculatedTotalPrice
            );
            reservationsDTOList.add(reservationsDTO);
        }
        return reservationsDTOList;
    }

    public ReservationsDTO createRandomReservation() {

        Instant simNow = timeConfig.instant();
        LocalDate simToday = LocalDateTime.ofInstant(simNow, timeConfig.getZone()).toLocalDate();

        int offset = 2 + random.nextInt(6); // bound = 6
        LocalDate checkIn = simToday.plusDays(offset);

        int nights = 1 + random.nextInt(3); // bound = 3
        LocalDate checkOut = checkIn.plusDays(nights);

        List<Camps> allCamps = campRepository.findAll();
        if (allCamps.isEmpty()) {
            logger.debug("No camps found in DB. Skipping.");
            return null;
        }

        Camps chosenCamps = pickCampWithCapacity(allCamps, checkIn, checkOut);
        if (chosenCamps == null) {
            logger.debug("All camps are full in that date range. No reservations created.");
            return null;
        }

        List<Customers> allCustomers = customerRepository.findAll();
        if (allCustomers.isEmpty()) {
            logger.debug("No customers found in DB. Skipping.");
            return null;
        }

        Customers randomCustomers = allCustomers.get(random.nextInt(allCustomers.size())); // bound > 0

        int requestedTents = 1 + random.nextInt(2); // bound = 2

        UUID reservationId = UUID.randomUUID();
        Reservations reservations = new Reservations(
                reservationId,
                Objects.requireNonNull(chosenCamps).getCampId(),
                randomCustomers.getCustomerId(),
                checkIn,
                checkOut,
                status.RESERVED,
                requestedTents,
                0 // 初期値として0
        );

        List<Rentals> chosenRentals = new ArrayList<>();
        List<Integer> chosenQuantities = new ArrayList<>();
        List<CampRentals> campRentalsList = campRentalRepository.findByCampId(chosenCamps.getCampId());
        if (!campRentalsList.isEmpty()) {
            int howMany = 1 + random.nextInt(2); // bound = 2
            for (int i = 0; i < howMany && !campRentalsList.isEmpty(); i++) {
                CampRentals campRentals = campRentalsList.get(random.nextInt(campRentalsList.size()));
                rentalRepository.findByRentalId(campRentals.getRentalId()).ifPresent(rental -> {
                    chosenRentals.add(rental);
                    chosenQuantities.add(1 + random.nextInt(3)); // bound = 3
                });
            }
        } else {
            logger.debug("Camps has no rentals. continuing with empty rental list.");
        }

        int calculatedTotalPrice = calculateTotalPrice(reservations, chosenCamps, chosenRentals, chosenQuantities);
        reservations.setTotalPrice(calculatedTotalPrice);

        reservationRepository.save(reservations);

        for (int i = 0; i < chosenRentals.size(); i++) {
            Rentals chosenRental = chosenRentals.get(i);
            int quantity = chosenQuantities.get(i);

            ReservationRentals reservationRental = new ReservationRentals(
                    reservationId,
                    chosenRental.getRentalId(),
                    quantity
            );
            reservationRentalRepository.save(reservationRental);
        }


        return new ReservationsDTO().buildDto(
                reservations,
                chosenCamps,
                randomCustomers,
                chosenRentals,
                chosenQuantities,
                calculatedTotalPrice
        );
    }

    private Camps pickCampWithCapacity(List<Camps> camps, LocalDate checkIn, LocalDate checkOut) {
        for (int i = 0; i < 50; i++) {
            if (camps.isEmpty()) {
                logger.warn("No camps found in DB. Skipping.");
                break;
            }
            Camps candidate = camps.get(random.nextInt(camps.size()));// bound > 0
            int requestedTents = 1 + random.nextInt(2); // bound = 2
            if (canReserve(candidate, checkIn, checkOut, requestedTents)) {
                return candidate;
            }
        }
        return null;
    }

    private boolean canReserve(Camps camps, LocalDate checkIn, LocalDate checkOut, int requestedTents) {

        List<Reservations> existing = reservationRepository.findByCampId(camps.getCampId());
        int used = 0;
        for (Reservations reservations : existing) {
            if (isOverlap(reservations.getCheckInDate(), reservations.getCheckOutDate(), checkIn, checkOut)) {
                used += (reservations.getNumberOfTents() != null ? reservations.getNumberOfTents() : 0);
            }
        }
        Integer capacity = camps.getCapacity();
        if (capacity == null) capacity = 0;
        return (used + requestedTents) <= capacity;
    }

    private boolean isOverlap(LocalDate firstStartDate, LocalDate firstEndDate, LocalDate secondStartDate, LocalDate secondEndDate) {
        return !(firstEndDate.isBefore(secondStartDate) || secondEndDate.isBefore(firstStartDate));
    }

    private int calculateTotalPrice(Reservations reservations, Camps camps, List<Rentals> rentals, List<Integer> quantities) {
        long days = ChronoUnit.DAYS.between(reservations.getCheckInDate(), reservations.getCheckOutDate());
        if (days == 0) days = 1;

        int campCost = (camps.getPrice() != null ? camps.getPrice() : 0)
                * (reservations.getNumberOfTents() != null ? reservations.getNumberOfTents() : 1)
                * (int) days;

        int rentalCost = 0;
        for (int i = 0; i < rentals.size(); i++) {
            Rentals rental = rentals.get(i);
            int quantity = i < quantities.size() ? quantities.get(i) : 1;
            rentalCost += (rental.getPricePerDay() != null ? rental.getPricePerDay() : 0)
                    * quantity
                    * (int) days;
        }

        return campCost + rentalCost;
    }
}
