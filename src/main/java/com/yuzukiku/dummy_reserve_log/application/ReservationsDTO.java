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

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationsDTO {

    private UUID id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String status;
    private int numberOfTents;
    private CampDto camp;
    private CustomerDto customer;
    private List<RentalDto> rentals;
    private int total;
    private int givenPoint;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CampDto {
        private UUID id;
        private int price;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerDto {
        private UUID id;
        private String name;
        private String address;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RentalDto {
        private UUID id;
        private String name;
        private int unitPrice;
        private int quantity;
        private int subtotal;
    }

    public ReservationsDTO buildDto(
            Reservations reservations,
            Camps camps,
            Customers customers,
            List<Rentals> rentals,
            List<Integer> quantities,
            int totalPrice
    ) {
        ReservationsDTO reservationsDTO = new ReservationsDTO();
        reservationsDTO.setId(reservations.getReservationId());
        reservationsDTO.setCheckInDate(reservations.getCheckInDate());
        reservationsDTO.setCheckOutDate(reservations.getCheckOutDate());
        reservationsDTO.setStatus(reservations.getStatus().toString());
        reservationsDTO.setNumberOfTents(
                reservations.getNumberOfTents() != null
                        ? reservations.getNumberOfTents()
                        : 1);

        CampDto campDto = new CampDto();
        campDto.setId(camps.getCampId());
        campDto.setPrice(
                camps.getPrice() != null
                        ? camps.getPrice()
                        : 0);
        reservationsDTO.setCamp(campDto);

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customers.getCustomerId());
        customerDto.setName(customers.getName());
        customerDto.setAddress(customers.getAddress());
        reservationsDTO.setCustomer(customerDto);

        long days = ChronoUnit.DAYS.between(reservations.getCheckInDate(), reservations.getCheckOutDate());
        if (days == 0) days = 1;

        // ランダムに選択されたレンタル品が重複する可能性を防ぐため
        Map<UUID, RentalDto> rentalsDtoMap = new HashMap<>();
        for (int i = 0; i < rentals.size() ; i++) {
            Rentals rental = rentals.get(i);
            int quantity = i < quantities.size() ? quantities.get(i) : 1;

            RentalDto rentalDto = rentalsDtoMap.getOrDefault(rental.getRentalId(), new RentalDto());
            rentalDto.setId(rental.getRentalId());
            rentalDto.setName(rental.getName());
            rentalDto.setUnitPrice(
                    rental.getPricePerDay() != null
                            ? rental.getPricePerDay()
                            : 0);
            rentalDto.setQuantity(rentalDto.getQuantity() + quantity);
            int currentSubtotal = rentalDto.getSubtotal();
            rentalDto.setSubtotal(currentSubtotal + rentalDto.getUnitPrice() * quantity * (int) days);

            rentalsDtoMap.put(rental.getRentalId(), rentalDto);
        }
        reservationsDTO.setRentals(new ArrayList<>(rentalsDtoMap.values()));

        reservationsDTO.setTotal(totalPrice);
        reservationsDTO.setGivenPoint(totalPrice/100);

        return reservationsDTO;
    }
}
