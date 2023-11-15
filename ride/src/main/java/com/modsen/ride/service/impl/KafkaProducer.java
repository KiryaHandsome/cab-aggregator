package com.modsen.ride.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<Integer, String> kafkaTemplate;

    public <T> void sendMessage(String topic, T message) {

    }
}