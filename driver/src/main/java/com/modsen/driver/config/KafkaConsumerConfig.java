package com.modsen.driver.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final ConcurrentKafkaListenerContainerFactory<?, ?> listenerContainerFactory;

    @PostConstruct
    public void postConstruct() {
        listenerContainerFactory.getContainerProperties().setObservationEnabled(true);
    }

    @Bean
    public NewTopic rideOrderedTopic() {
        return TopicBuilder
                .name("ride-ordered")
                .build();
    }
}
