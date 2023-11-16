package com.modsen.driver.service.impl;

import com.modsen.driver.dto.event.RideEvent;
import com.modsen.driver.dto.request.DriverUpdate;
import com.modsen.driver.model.Status;
import com.modsen.driver.service.DriverService;
import com.modsen.driver.service.RideExecutor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideExecutorStub implements RideExecutor {

    private final KafkaProducer kafkaProducer;
    private final DriverService driverService;

    @Async
    @SneakyThrows
    public void ride(RideEvent event) {
        Thread.sleep(5000);
        DriverUpdate request = DriverUpdate.builder()
                .status(Status.AVAILABLE)
                .build();
        driverService.update(event.getDriverId(), request);
        kafkaProducer.sendMessage("ride-ended", event);
    }
}
