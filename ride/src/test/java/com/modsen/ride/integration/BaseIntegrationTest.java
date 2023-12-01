package com.modsen.ride.integration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles("test")
@Testcontainers
public class BaseIntegrationTest {

    public static final Integer MONGO_PORT = 27017;
    public static final String MONGO_IMAGE_NAME = "mongo:7.0";
    public static final String KAFKA_IMAGE_NAME = "confluentinc/cp-kafka:7.5.1";

    @Container
    protected static final MongoDBContainer mongoContainer = new MongoDBContainer(
            DockerImageName.parse(MONGO_IMAGE_NAME)
    )
            .withExposedPorts(MONGO_PORT);

    @Container
    protected static final KafkaContainer kafkaContainer = new KafkaContainer(
            DockerImageName.parse(KAFKA_IMAGE_NAME)
    );

    @DynamicPropertySource
    static void init(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.data.mongodb.host", mongoContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoContainer::getFirstMappedPort);
    }

    @Test
    void testMongoConnection() {
        assertThat(mongoContainer.getConnectionString()).isNotNull();
        log.info("Mongo container connection uri: " + mongoContainer.getConnectionString());
    }
}
