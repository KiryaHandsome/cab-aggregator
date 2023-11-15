package com.modsen.ride.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic rideStartedTopic() {
        return TopicBuilder
                .name("ride-started")
                .build();
    }

    @Bean
    public NewTopic rideEndedTopic() {
        return TopicBuilder
                .name("ride-ended")
                .build();
    }
}
