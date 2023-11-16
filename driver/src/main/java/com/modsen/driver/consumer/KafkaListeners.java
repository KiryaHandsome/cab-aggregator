package com.modsen.driver.consumer;

import com.modsen.driver.util.JsonUtil;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(topics = "ride-ordered", groupId = "driver-service")
    public void rideOrderedListener(String message) {
        var event = JsonUtil.fromJson(message, String.class);
        // todo:
        //  implement algorithm to choose random available driver
        //  and send message about starting ride
    }
}
