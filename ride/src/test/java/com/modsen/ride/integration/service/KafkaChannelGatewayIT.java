package com.modsen.ride.integration.service;

import com.modsen.ride.dto.RideDto;
import com.modsen.ride.dto.request.RideRequest;
import com.modsen.ride.integration.BaseIntegrationTest;
import com.modsen.ride.service.KafkaChannelGateway;
import com.modsen.ride.util.TestData;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class KafkaChannelGatewayIT extends BaseIntegrationTest {

    @Autowired
    private KafkaChannelGateway kafkaChannelGateway;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Test
    void sendToRideOrdered_shouldSendMessageToRideOrderedTopic() {
        KafkaConsumer<String, RideRequest> consumer = setUpKafkaConsumer("ride-ordered", RideRequest.class);

        RideRequest request = TestData.rideRequest();
        kafkaChannelGateway.sendToRideOrdered(request);

        ConsumerRecords<String, RideRequest> records = consumer.poll(Duration.of(10, TimeUnit.SECONDS.toChronoUnit()));

        assertThat(records.count()).isEqualTo(1);
        records.forEach(r -> assertThat(r.value()).isEqualTo(request));
    }

    @Test
    void sendToRidePayment_shouldSendMessageToRidePayment() {
        KafkaConsumer<String, RideDto> consumer = setUpKafkaConsumer("ride-payment", RideDto.class);

        RideDto rideInfo = TestData.defaultRideDto();
        kafkaChannelGateway.sendToRidePayment(rideInfo);

        ConsumerRecords<String, RideDto> records = consumer.poll(Duration.of(10, TimeUnit.SECONDS.toChronoUnit()));

        assertThat(records.count()).isEqualTo(1);
        records.forEach(r -> assertThat(r.value()).isEqualTo(rideInfo));
    }

    private <T> KafkaConsumer<String, T> setUpKafkaConsumer(String topicName, Class<T> valueType) {
        JsonDeserializer<T> deserializer = new JsonDeserializer<>();
        deserializer.addTrustedPackages("*");
        KafkaConsumer<String, T> consumer = new KafkaConsumer<>(ImmutableMap.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ConsumerConfig.GROUP_ID_CONFIG, topicName + "kafka-channel-gateway-test",
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
        ), new StringDeserializer(), deserializer);
        consumer.subscribe(Collections.singletonList(topicName));
        return consumer;
    }
}
