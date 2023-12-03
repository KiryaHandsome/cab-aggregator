package com.modsen.payment.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfig {

    @Bean
    public NewTopic ridePaymentTopic() {
        return TopicBuilder
                .name("ride-payment")
                .build();
    }

    @Bean
    public NewTopic paymentResultTopic() {
        return TopicBuilder
                .name("payment-result")
                .build();
    }
}
