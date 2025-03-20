package com.yuzukiku.dummy_reserve_log.application;

import com.yuzukiku.dummy_reserve_log.domain.entity.Camps;
import com.yuzukiku.dummy_reserve_log.domain.entity.Customers;
import com.yuzukiku.dummy_reserve_log.domain.entity.Rentals;
import com.yuzukiku.dummy_reserve_log.domain.entity.Reservations;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.IntStream;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationsDTO {

    private UUID id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String status;
    private int numberOfTents;
    private CampDTO camp;
    private CustomerDTO customer;
    private List<RentalDTO> rentals;
    private int total;
    private int givenPoint;

    public static ReservationsDTO buildDTO(
            Reservations reservations,
            Camps camps,
            Customers customers,
            List<Rentals> rentals,
            List<Integer> quantities,
            int totalPrice
    ) {
        ReservationsDTO dto = new ReservationsDTO();
        dto.id = reservations.getReservationId();
        dto.checkInDate = reservations.getCheckInDate();
        dto.checkOutDate = reservations.getCheckOutDate();
        dto.status = reservations.getStatus().toString();
        dto.numberOfTents = Optional.ofNullable(reservations.getNumberOfTents()).orElse(1);

        dto.camp = new CampDTO(
                camps.getCampId(),
                Optional.ofNullable(camps.getPrice()).orElse(0)
        );
        dto.customer = new CustomerDTO(
                customers.getCustomerId(),
                customers.getName(),
                customers.getAddress()
        );

        long days = ChronoUnit.DAYS.between(dto.checkInDate, dto.checkOutDate);
        days = days == 0 ? 1 : days;

        Map<UUID, RentalDTO> rentalMap = new HashMap<>();

        long finalDays = days;
        IntStream.range(0, rentals.size()).forEach(i -> {
            Rentals rental = rentals.get(i);
            int quantity = i < quantities.size() ? quantities.get(i) : 1;
            rentalMap.compute(rental.getRentalId(), (key, existing) -> {
                int unitPrice = Optional.ofNullable(rental.getPricePerDay()).orElse(0);
                if (existing == null) {
                    return new RentalDTO(
                            rental.getRentalId(),
                            rental.getName(),
                            unitPrice,
                            quantity,
                            unitPrice * quantity * (int) finalDays
                    );
                } else {
                    existing.setQuantity(existing.getQuantity() + quantity);
                    existing.setSubtotal(existing.getSubtotal() + unitPrice * quantity * (int) finalDays);
                    return existing;
                }
            });
        });
        dto.rentals = new ArrayList<>(rentalMap.values());
        dto.total = totalPrice;
        dto.givenPoint = totalPrice / 100;
        return dto;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CampDTO {
        private UUID id;
        private int price;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerDTO {
        private UUID id;
        private String name;
        private String address;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RentalDTO {
        private UUID id;
        private String name;
        private int unitPrice;
        private int quantity;
        private int subtotal;
    }
}
