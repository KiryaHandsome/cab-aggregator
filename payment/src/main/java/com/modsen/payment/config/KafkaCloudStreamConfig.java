package com.modsen.payment.config;

import com.modsen.payment.dto.PaymentEvent;
import com.modsen.payment.dto.RideInfo;
import com.modsen.payment.service.PaymentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.function.Function;

@Configuration
public class KafkaCloudStreamConfig {

    @Bean
    public Function<RideInfo, Message<PaymentEvent>> processRidePayment(PaymentService paymentService) {
        return r -> MessageBuilder
                .withPayload(paymentService.payForRide(r))
                .setHeader(DefaultJackson2JavaTypeMapper.DEFAULT_CLASSID_FIELD_NAME, PaymentEvent.class.getCanonicalName())
                .build();
    }
}
