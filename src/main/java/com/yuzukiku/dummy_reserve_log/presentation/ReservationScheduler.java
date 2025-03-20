package com.yuzukiku.dummy_reserve_log.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class ReservationScheduler {

    private final ReservationKafkaProducer reservationKafkaProducer;

    @Scheduled(fixedRate = 500)
    public void create() {
        reservationKafkaProducer.send();
    }
}