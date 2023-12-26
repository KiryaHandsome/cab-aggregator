package com.modsen.e2e.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.PullPolicy;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

@Slf4j
@ContextConfiguration(initializers = E2ESuite.Initializer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class E2ESuite {

    private static final String KAFKA_IMAGE = "confluentinc/cp-kafka:7.5.1";
    private static final String DRIVER_IMAGE = "kiryahandsome/driver:latest";
    private static final String RIDE_IMAGE = "kiryahandsome/ride:latest";
    private static final String POSTGRES_IMAGE = "postgres:15-alpine";
    private static final String MONGO_IMAGE = "mongo:7.0";
    private static final Network SHARED_NETWORK = Network.newNetwork();
    protected static KafkaContainer KAFKA;
    protected static PostgreSQLContainer<?> DRIVER_POSTGRES;
    protected static GenericContainer<?> RIDE;
    protected static GenericContainer<?> DRIVER;
    protected static MongoDBContainer RIDE_MONGO;

    static class Initializer implements
            ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext context) {
            KAFKA = createKafkaContainer();
            DRIVER_POSTGRES = createPostgresContainer("driver_db", "driver");
            RIDE_MONGO = createMongoContainer("ride");
            Startables.deepStart(KAFKA, DRIVER_POSTGRES, RIDE_MONGO).join();

            DRIVER = createDriverServiceContainer();
            RIDE = createRideServiceContainer();

            Startables.deepStart(DRIVER, RIDE).join();
        }

        private MongoDBContainer createMongoContainer(String databaseName) {
            return new MongoDBContainer(DockerImageName.parse(MONGO_IMAGE))
                    .withNetwork(SHARED_NETWORK)
                    .withNetworkAliases("ride_db")
                    .withExposedPorts(27017)
                    .withEnv("MONGO_INITDB_DATABASE", databaseName);
        }

        private GenericContainer<?> createRideServiceContainer() {
            return new GenericContainer(DockerImageName.parse(RIDE_IMAGE))
                    .withNetwork(SHARED_NETWORK)
                    .dependsOn(RIDE_MONGO, KAFKA, DRIVER)
                    .withExposedPorts(8082)
                    .withEnv("SPRING_CLOUD_DISCOVERY_ENABLED", "false")
                    .withEnv("EUREKA_CLIENT_REGISTER_WITH_EUREKA", "false")
                    .withEnv("SPRING_KAFKA_BOOTSTRAP_SERVERS", "kafka:9092")
                    .withEnv("SPRING_DATA_MONGODB_HOST", "ride_db")
                    .withEnv("SPRING_DATA_MONGODB_PORT", "27017")
                    .withEnv("DRIVER_URL", "driver:8081")
                    .withEnv("LOGGING_LEVEL_ROOT", "debug")
//                    .withLogConsumer(new Slf4jLogConsumer(log))
                    .waitingFor(
                            Wait.forHttp("/actuator/health")
                                    .forStatusCode(200)
                    )
                    .withImagePullPolicy(PullPolicy.alwaysPull());
        }

        private PostgreSQLContainer<?> createPostgresContainer(String hostName, String databaseName) {
            return (PostgreSQLContainer<?>) new PostgreSQLContainer(DockerImageName.parse(POSTGRES_IMAGE))
                    .withUsername("postgres")
                    .withPassword("postgres")
                    .withDatabaseName(databaseName)
                    .withNetwork(SHARED_NETWORK)
                    .withExposedPorts(5432)
                    .withNetworkAliases(hostName);
        }

        private KafkaContainer createKafkaContainer() {
            return new KafkaContainer(DockerImageName.parse(KAFKA_IMAGE))
                    .withNetwork(SHARED_NETWORK)
                    .withNetworkAliases("kafka")
//                    .withLogConsumer(new Slf4jLogConsumer(log))
                    ;
        }

        private GenericContainer createDriverServiceContainer() {
            return new GenericContainer(DockerImageName.parse(DRIVER_IMAGE))
                    .dependsOn(DRIVER_POSTGRES, KAFKA)
                    .withNetwork(SHARED_NETWORK)
                    .withNetworkAliases("driver")
                    .withExposedPorts(8081)
                    .withEnv("SPRING_CLOUD_DISCOVERY_ENABLED", "false")
                    .withEnv("EUREKA_CLIENT_REGISTER_WITH_EUREKA", "false")
                    .withEnv("SPRING_KAFKA_BOOTSTRAP_SERVERS", "kafka:9092")
                    .withEnv("SPRING_DATASOURCE_URL", "jdbc:postgresql://driver_db:5432/driver")
                    .withEnv("LOGGING_LEVEL_ROOT", "debug")
                    .withLogConsumer(new Slf4jLogConsumer(log))
                    .withImagePullPolicy(PullPolicy.alwaysPull());
        }
    }
}
