package com.yuzukiku.dummy_reserve_log.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuzukiku.dummy_reserve_log.application.ReservationService;
import com.yuzukiku.dummy_reserve_log.application.ReservationsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReservationKafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(ReservationKafkaProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ReservationService reservationService;
    private final ObjectMapper objectMapper;

    public ReservationKafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ReservationService reservationService, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.reservationService = reservationService;
        this.objectMapper = objectMapper;
    }

    public void send() {
        try {
            ReservationsDTO reservationsDTO = reservationService.createRandomReservation();

            if (reservationsDTO == null) {
                logger.warn("ReservationsDTO is null. Skipping Kafka send.");
                return;
            }

            String json = objectMapper.writeValueAsString(reservationsDTO);
            kafkaTemplate.send("reservations-log", reservationsDTO.getId().toString(), json);

            logger.debug("Sent to Kafka => key={}, payload={}", reservationsDTO.getId(), json);
        } catch (JsonProcessingException e) {
            logger.error("Failed to send ReservationDTO to Kafka", e);
        }
    }
}
