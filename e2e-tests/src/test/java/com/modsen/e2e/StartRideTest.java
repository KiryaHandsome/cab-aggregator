package com.modsen.e2e;

import com.modsen.e2e.dto.ErrorResponse;
import com.modsen.e2e.dto.WaitingRideResponse;
import com.modsen.e2e.util.E2ESuite;
import com.modsen.e2e.util.RideTestClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class StartRideTest extends E2ESuite {

    @Autowired
    private TestRestTemplate restTemplate;
    private static RideTestClient rideTestClient;
    private WaitingRideResponse waitingRide;
    private final Integer passengerId = 1;
    private final String from = "From";
    private final String to = "To";

    @PostConstruct
    public void setUp() {
        rideTestClient = new RideTestClient(restTemplate, RIDE.getHost(), RIDE.getFirstMappedPort());
        ResponseEntity<WaitingRideResponse> response = rideTestClient.bookRide(passengerId, from, to);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        waitingRide = response.getBody();
    }

    @Test
    void withNonExistingRideId_shouldNotStartRide() {
        Integer driverId = 1;
        String rideId = "unexistingid";

        ResponseEntity<ErrorResponse> response = rideTestClient.startRide(rideId, driverId, ErrorResponse.class);

        log.info("Response withNonExistingRideId: {}", response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void withBusyDriver_shouldNotStartRide() {
        Integer driverId = 1;
        String rideId = waitingRide.getId();

        ResponseEntity<ErrorResponse> response = rideTestClient.startRide(rideId, driverId, ErrorResponse.class);

        log.info("Response withBusyDriver: {}", response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
