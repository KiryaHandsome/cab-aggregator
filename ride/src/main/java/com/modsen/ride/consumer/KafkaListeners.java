package com.modsen.ride.consumer;


import com.modsen.ride.dto.RideEvent;
import com.modsen.ride.service.RideService;
import com.modsen.ride.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final RideService rideService;

    @KafkaListener(topics = "ride-started", groupId = "ride-service")
    public void rideStartedListener(String message) {
        log.info("Received message from topic <ride-started>: {}", message);
        RideEvent event = JsonUtil.fromJson(message, RideEvent.class);
        rideService.startRide(event);
    }

    @KafkaListener(topics = "ride-ended", groupId = "ride-service")
    public void rideEndedListener(String message) {
        log.info("Received message from topic <ride-ended>: {}", message);
        RideEvent event = JsonUtil.fromJson(message, RideEvent.class);
        rideService.endRide(event);
    }
}
