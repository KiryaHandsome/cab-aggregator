package com.modsen.e2etests.util;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

public class E2ESuite {

    private static final String KAFKA_IMAGE = "confluentinc/cp-kafka:7.5.1";
    private static final String DRIVER_IMAGE = "kiryahandsome/driver:latest";
    private static final String RIDE_IMAGE = "kiryahandsome/ride:latest";
    private static final String POSTGRES_IMAGE = "postgres:15-alpine";
    private static final String MONGO_IMAGE = "mongo:7.0";
    private static final Network SHARED_NETWORK = Network.newNetwork();
    private static KafkaContainer KAFKA;
    private static PostgreSQLContainer DRIVER_POSTGRES;
    protected static GenericContainer RIDE;
    private static GenericContainer DRIVER;
    private static MongoDBContainer RIDE_MONGO;

    static class Initializer implements
            ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext context) {
//            final var environment = context.getEnvironment();
            KAFKA = createKafkaContainer();
            DRIVER_POSTGRES = createPostgresContainer("driver_db", "driver");
            RIDE_MONGO = createMongoContainer("ride");

            Startables.deepStart(KAFKA, DRIVER_POSTGRES).join();
            DRIVER = createDriverServiceContainer();
            RIDE = createRideServiceContainer();

            Startables.deepStart(DRIVER, RIDE).join();
        }

        private MongoDBContainer createMongoContainer(String databaseName) {
            return new MongoDBContainer(DockerImageName.parse(MONGO_IMAGE))
                    .withNetwork(SHARED_NETWORK)
                    .withExposedPorts(27017)
                    .withEnv("MONGO_INITDB_DATABASE", databaseName);
        }

        private GenericContainer createRideServiceContainer() {
            return new GenericContainer(DockerImageName.parse(RIDE_IMAGE))
                    .withNetwork(SHARED_NETWORK)
                    .withEnv("SPRING_DATA_MONGODB_HOST", RIDE_MONGO.getHost())
                    .withEnv("SPRING_DATA_MONGODB_PORT", String.valueOf(RIDE_MONGO.getMappedPort(27017)));
        }

        private PostgreSQLContainer createPostgresContainer(String hostName, String databaseName) {
            return (PostgreSQLContainer) new PostgreSQLContainer(DockerImageName.parse(POSTGRES_IMAGE))
                    .withNetwork(SHARED_NETWORK)
                    .withExposedPorts(5432)
                    .withNetworkAliases(hostName)
                    .withEnv("POSTGRES_DB", databaseName)
                    .withEnv("POSTGRES_USERNAME", "postgres")
                    .withEnv("POSTGRES_PASSWORD", "postgres");
        }

        private KafkaContainer createKafkaContainer() {
            return new KafkaContainer(DockerImageName.parse(KAFKA_IMAGE))
                    .withNetwork(SHARED_NETWORK)
                    .withNetworkAliases("kafka")
                    .withExposedPorts(9092);
        }

        private GenericContainer createDriverServiceContainer() {
            return new GenericContainer(DockerImageName.parse(DRIVER_IMAGE))
                    .withNetwork(SHARED_NETWORK)
                    .withNetworkAliases("driver")
                    .withEnv("SPRING_KAFKA_BOOTSTRAP_SERVERS", KAFKA.getBootstrapServers())
                    .withEnv("SPRING_DATASOURCE_URL", DRIVER_POSTGRES.getJdbcUrl());
        }
    }
}
