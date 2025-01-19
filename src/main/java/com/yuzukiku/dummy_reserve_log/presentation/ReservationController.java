package com.yuzukiku.dummy_reserve_log.presentation;

import com.yuzukiku.dummy_reserve_log.application.ReservationService;
import com.yuzukiku.dummy_reserve_log.application.ReservationsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public List<ReservationsDTO> getReservations() {
        return reservationService.searchList();
    }
}
