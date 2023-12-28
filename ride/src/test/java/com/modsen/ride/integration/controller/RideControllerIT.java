package com.modsen.ride.integration.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.Body;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.modsen.ride.dto.DriverStatus;
import com.modsen.ride.dto.RideDto;
import com.modsen.ride.dto.response.DriverResponse;
import com.modsen.ride.dto.response.WaitingRideResponse;
import com.modsen.ride.integration.BaseIntegrationTest;
import com.modsen.ride.integration.util.RideTestClient;
import com.modsen.ride.util.TestData;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.stream.IntStream;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.patchRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RideControllerIT extends BaseIntegrationTest {

    @RegisterExtension
    static WireMockExtension DRIVER_SERVICE = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig()
                    .port(8081))
            .build();

    @Value("${driver.path}")
    private String driverPath;
    private RideTestClient rideTestClient;

    @Value("${resilience4j.circuitbreaker.instances.driver.minimumNumberOfCalls}")
    private Integer minimumNumberOfCalls;

    @Autowired
    private TestRestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;
    private CircuitBreaker driverCircuitBreaker;

    @PostConstruct
    public void setUp() {
        rideTestClient = new RideTestClient(restTemplate);
    }

    @BeforeEach
    public void resetCircuitBreaker() {
        driverCircuitBreaker = circuitBreakerRegistry.circuitBreaker("driver");
        driverCircuitBreaker.reset();
        driverCircuitBreaker.transitionToClosedState();
    }

    @Test
    public void check_startRide_circuitBreaker() {
        Integer driverId = 1;
        String driverPath = this.driverPath + "/" + driverId;
        DRIVER_SERVICE.stubFor(WireMock.get(driverPath)
                .willReturn(serverError()));
        WaitingRideResponse waitingRide = rideTestClient.bookRide(TestData.PASSENGER_ID, TestData.FROM, TestData.TO);

        IntStream.rangeClosed(1, minimumNumberOfCalls).forEach(i -> {
            ResponseEntity<Void> response = rideTestClient.startRide(waitingRide.getId(),
                    driverId, Void.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        });

        IntStream.rangeClosed(1, minimumNumberOfCalls).forEach(i -> {
            ResponseEntity<Void> response = rideTestClient.startRide(waitingRide.getId(),
                    driverId, Void.class);
            log.info("CB state: {}", driverCircuitBreaker.getState());
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        });

        DRIVER_SERVICE.verify(minimumNumberOfCalls, getRequestedFor(urlEqualTo(driverPath)));
    }

    @Test
    @SneakyThrows
    public void check_endRide_circuitBreaker() {
        Integer driverId = 2;
        String driverPath = this.driverPath + "/" + driverId;
        DriverResponse driverResponseStub = new DriverResponse(driverId, "name", "surname", "email", "phone", DriverStatus.AVAILABLE);
        DRIVER_SERVICE.stubFor(WireMock.get(driverPath)
                .willReturn(aResponse()
                        .withResponseBody(Body.fromJsonBytes(objectMapper.writeValueAsBytes(driverResponseStub)))
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())));
        DRIVER_SERVICE.stubFor(WireMock.patch(driverPath)
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())));
        WaitingRideResponse waitingRide = rideTestClient.bookRide(TestData.PASSENGER_ID, TestData.FROM, TestData.TO);
        ResponseEntity<RideDto> startResponse = rideTestClient.startRide(waitingRide.getId(), driverId, RideDto.class);
        assertThat(startResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        RideDto ride = startResponse.getBody();

        DRIVER_SERVICE.stubFor(WireMock.patch(driverPath)
                .willReturn(serverError()));

        IntStream.rangeClosed(1, minimumNumberOfCalls - 1).forEach(i -> {
            ResponseEntity<Void> response = rideTestClient.endRide(ride.getId(), Void.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        });

        IntStream.rangeClosed(1, minimumNumberOfCalls).forEach(i -> {
            ResponseEntity<Void> response = rideTestClient.endRide(ride.getId(), Void.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        });

        // minimumNumberOfCalls = 1 call when starting ride + (minimumNumberOfCalls - 1) calls in cycle
        DRIVER_SERVICE.verify(minimumNumberOfCalls, patchRequestedFor(urlEqualTo(driverPath)));
        DRIVER_SERVICE.verify(1, getRequestedFor(urlEqualTo(driverPath)));
    }
}
