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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
     * 1999-02-11T00:00:00Z を基準とし、UTC, 8640倍で時刻を生成する
     * (TimeConfig は Clock を継承している)
     */
    private final TimeConfig timeConfig = new TimeConfig(
            Instant.parse("1999-02-11T00:00:00Z"),
            ZoneId.of("UTC"),
            8640
    );

    public List<ReservationsDTO> searchList() {
        return reservationRepository.findAll().stream()
                .map(reservation -> {
                    Camps camps = campRepository.findByCampId(reservation.getCampId()).orElse(null);
                    Customers customers = customerRepository.findByCustomerId(reservation.getCustomerId()).orElse(null);
                    if (camps == null || customers == null) {
                        logger.warn("Camp or Customer not found for reservation ID: {}", reservation.getReservationId());
                        return null;
                    }

                    List<ReservationRentals> reservationRentals = reservationRentalRepository.findByReservationId(reservation.getReservationId());
                    List<Rentals> rentals = new ArrayList<>();
                    List<Integer> quantities = new ArrayList<>();
                    reservationRentals.forEach(rr -> {
                        rentalRepository.findByRentalId(rr.getRentalId()).ifPresent(rentals::add);
                        quantities.add(rr.getQuantity());
                    });

                    int calculatedTotalPrice = calculateTotalPrice(reservation, camps, rentals, quantities);
                    return ReservationsDTO.buildDTO(
                            reservation,
                            camps,
                            customers,
                            rentals,
                            quantities,
                            calculatedTotalPrice
                    );
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public ReservationsDTO createRandomReservation() {
        Instant simNow = timeConfig.instant();
        LocalDate simToday = LocalDateTime.ofInstant(simNow, timeConfig.getZone()).toLocalDate();

        int offset = 2 + random.nextInt(6);
        LocalDate checkIn = simToday.plusDays(offset);

        int nights = 1 + random.nextInt(3);
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
        Customers randomCustomer = allCustomers.get(random.nextInt(allCustomers.size()));

        int requestedTents = 1 + random.nextInt(2);

        UUID reservationId = UUID.randomUUID();
        Reservations reservations = new Reservations(
                reservationId,
                chosenCamps.getCampId(),
                randomCustomer.getCustomerId(),
                checkIn,
                checkOut,
                status.RESERVED,
                requestedTents,
                0 // 初期値
        );

        List<Rentals> chosenRentals = new ArrayList<>();
        List<Integer> chosenQuantities = new ArrayList<>();
        List<CampRentals> campRentalsList = campRentalRepository.findByCampId(chosenCamps.getCampId());
        if (!campRentalsList.isEmpty()) {
            int howMany = 1 + random.nextInt(2);
            IntStream.range(0, howMany).forEach(i -> {
                if (!campRentalsList.isEmpty()) {
                    CampRentals campRentals = campRentalsList.get(random.nextInt(campRentalsList.size()));
                    rentalRepository.findByRentalId(campRentals.getRentalId()).ifPresent(rental -> {
                        chosenRentals.add(rental);
                        chosenQuantities.add(1 + random.nextInt(3));
                    });
                }
            });
        } else {
            logger.debug("Camps has no rentals. Continuing with empty rental list.");
        }

        int calculatedTotalPrice = calculateTotalPrice(reservations, chosenCamps, chosenRentals, chosenQuantities);
        reservations.setTotalPrice(calculatedTotalPrice);
        reservationRepository.save(reservations);

        IntStream.range(0, chosenRentals.size()).forEach(i -> {
            Rentals chosenRental = chosenRentals.get(i);
            int quantity = chosenQuantities.get(i);
            ReservationRentals reservationRental = new ReservationRentals(
                    reservationId,
                    chosenRental.getRentalId(),
                    quantity
            );
            reservationRentalRepository.save(reservationRental);
        });

        return ReservationsDTO.buildDTO(
                reservations,
                chosenCamps,
                randomCustomer,
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
            Camps candidate = camps.get(random.nextInt(camps.size()));
            int requestedTents = 1 + random.nextInt(2);
            if (canReserve(candidate, checkIn, checkOut, requestedTents)) {
                return candidate;
            }
        }
        return null;
    }

    private boolean canReserve(Camps camps, LocalDate checkIn, LocalDate checkOut, int requestedTents) {
        List<Reservations> existing = reservationRepository.findByCampId(camps.getCampId());
        int used = existing.stream()
                .filter(reservation -> isOverlap(reservation.getCheckInDate(), reservation.getCheckOutDate(), checkIn, checkOut))
                .mapToInt(reservation -> Optional.ofNullable(reservation.getNumberOfTents()).orElse(0))
                .sum();
        int capacity = Optional.ofNullable(camps.getCapacity()).orElse(0);
        return (used + requestedTents) <= capacity;
    }

    private boolean isOverlap(LocalDate firstStartDate,
                              LocalDate firstEndDate,
                              LocalDate secondStartDate,
                              LocalDate secondEndDate) {
        return (firstEndDate.isAfter(secondStartDate) || firstEndDate.isEqual(secondStartDate))
                && (secondEndDate.isAfter(firstStartDate) || secondEndDate.isEqual(firstStartDate));
    }

    private int calculateTotalPrice(Reservations reservations, Camps camps, List<Rentals> rentals, List<Integer> quantities) {
        long days = ChronoUnit.DAYS.between(reservations.getCheckInDate(), reservations.getCheckOutDate());
        days = days == 0 ? 1 : days;

        int campCost = Optional.ofNullable(camps.getPrice()).orElse(0)
                * Optional.ofNullable(reservations.getNumberOfTents()).orElse(1)
                * (int) days;

        long finalDays = days;
        int rentalCost = IntStream.range(0, rentals.size())
                .map(i -> {
                    int quantity = i < quantities.size() ? quantities.get(i) : 1;
                    int pricePerDay = Optional.ofNullable(rentals.get(i).getPricePerDay()).orElse(0);
                    return pricePerDay * quantity * (int) finalDays;
                })
                .sum();

        return campCost + rentalCost;
    }
}
