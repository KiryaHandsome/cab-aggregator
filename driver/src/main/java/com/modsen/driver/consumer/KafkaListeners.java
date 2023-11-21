package com.modsen.driver.consumer;

import com.modsen.driver.dto.event.RideOrdered;
import com.modsen.driver.service.DriverNotifier;
import com.modsen.driver.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final DriverNotifier driverNotifier;

    @KafkaListener(topics = "ride-ordered", groupId = "driver-service")
    public void rideOrderedListener(String message) {
        log.info("Receive message from topic <ride-ordered>: {}", message);
        RideOrdered event = JsonUtil.fromJson(message, RideOrdered.class);
        driverNotifier.notifyAvailableDrivers(event);
    }
}
