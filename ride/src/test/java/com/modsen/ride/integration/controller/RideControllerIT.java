package com.modsen.ride.integration.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.modsen.ride.dto.response.WaitingRideResponse;
import com.modsen.ride.integration.BaseIntegrationTest;
import com.modsen.ride.integration.util.RideTestClient;
import com.modsen.ride.util.TestData;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.stream.IntStream;

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RideControllerIT extends BaseIntegrationTest {

    @RegisterExtension
    static WireMockExtension EXTERNAL_SERVICE = WireMockExtension.newInstance()
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

    @PostConstruct
    public void setUp() {
        rideTestClient = new RideTestClient(restTemplate);
    }

    @Test
    public void check_startRide_circuitBreaker() {
        Integer driverId = 1;
        String driverPath = this.driverPath + "/" + driverId;
        EXTERNAL_SERVICE.stubFor(WireMock.get(driverPath)
                .willReturn(serverError()));
        EXTERNAL_SERVICE.stubFor(WireMock.patch(driverPath)
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
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        });

        EXTERNAL_SERVICE.verify(minimumNumberOfCalls, getRequestedFor(urlEqualTo(driverPath)));
    }
}
