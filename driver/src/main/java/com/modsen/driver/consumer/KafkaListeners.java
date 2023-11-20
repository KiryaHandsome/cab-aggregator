package com.modsen.driver.consumer;

import com.modsen.driver.dto.event.RideEvent;
import com.modsen.driver.dto.event.RideOrdered;
import com.modsen.driver.dto.response.DriverResponse;
import com.modsen.driver.mapper.EventMapper;
import com.modsen.driver.service.DriverPicker;
import com.modsen.driver.service.RideExecutor;
import com.modsen.driver.service.impl.KafkaProducer;
import com.modsen.driver.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final EventMapper eventMapper;
    private final DriverPicker driverPicker;
    private final KafkaProducer kafkaProducer;
    private final RideExecutor rideExecutor;

    @KafkaListener(topics = "ride-ordered", groupId = "driver-service")
    public void rideOrderedListener(String message) {
        log.info("Receive message from topic <ride-ordered>: {}", message);
        RideOrdered event = JsonUtil.fromJson(message, RideOrdered.class);
        DriverResponse driver = driverPicker.pickAvailable();
        RideEvent rideEvent = eventMapper.toRideEvent(event);
        rideEvent.setDriverId(driver.getId());
        kafkaProducer.sendMessage("ride-started", rideEvent);
        rideExecutor.ride(rideEvent);
    }
}
