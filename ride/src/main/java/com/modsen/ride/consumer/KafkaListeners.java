package com.modsen.ride.consumer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaListeners {

    @KafkaListener(topics = "ride-started", groupId = "ride-service")
    public void rideStartedListener(String message) {
        // todo: process ride-started event from driver
    }

    @KafkaListener(topics = "ride-started", groupId = "ride-service")
    public void rideEndedListener(String message) {
        // todo: process ride-started event from driver
    }


}
