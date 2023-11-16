package com.modsen.driver.service.impl;

import com.modsen.driver.dto.event.RideEvent;
import com.modsen.driver.service.RideExecutor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideExecutorStub implements RideExecutor {

    private final KafkaProducer kafkaProducer;

    @Async
    @SneakyThrows
    public void ride(RideEvent event) {
        Thread.sleep(5000);
        kafkaProducer.sendMessage("ride-ended", event);
    }
}
