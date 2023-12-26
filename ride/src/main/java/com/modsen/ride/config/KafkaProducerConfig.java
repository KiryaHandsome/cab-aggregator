package com.modsen.ride.config;

import com.modsen.ride.dto.request.PaymentEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;

import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public IntegrationFlow sendToRidePaymentFlow() {
        return f -> f.channel("sendToRidePaymentChannel")
                .handle(Kafka.outboundChannelAdapter(kafkaTemplate())
                        .messageKey(MessageHeaders.ID)
                        .topic("ride-payment"));
    }

    @Bean
    public IntegrationFlow sendToRideOrderedFlow() {
        return f -> f.channel("sendToRideOrderedChannel")
                .handle(Kafka.outboundChannelAdapter(kafkaTemplate())
                        .messageKey(MessageHeaders.ID)
                        .topic("ride-ordered"));
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaConfigs());
    }

    @Bean
    public MessageChannel sendToRidePaymentChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel sendToRideOrderedChannel() {
        return new DirectChannel();
    }

    @Bean
    public Map<String, Object> kafkaConfigs() {
        return Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
                JsonDeserializer.TYPE_MAPPINGS, "paymentEvent:" + PaymentEvent.class.getName()
        );
    }
}