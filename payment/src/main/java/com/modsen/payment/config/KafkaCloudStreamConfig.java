package com.modsen.payment.config;

import com.modsen.payment.dto.PaymentEvent;
import com.modsen.payment.dto.RideInfo;
import com.modsen.payment.service.PaymentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class KafkaCloudStreamConfig {

    @Bean
    public Function<RideInfo, PaymentEvent> processRidePayment(PaymentService paymentService) {
        return paymentService::payForRide;
    }

}
