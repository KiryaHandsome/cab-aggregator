package com.modsen.e2e;

import com.modsen.e2e.dto.ErrorResponse;
import com.modsen.e2e.dto.RideDto;
import com.modsen.e2e.dto.WaitingRideResponse;
import com.modsen.e2e.util.DriverTestClient;
import com.modsen.e2e.util.E2ESuite;
import com.modsen.e2e.util.RideTestClient;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
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
    private static DriverTestClient driverTestClient;
    private WaitingRideResponse waitingRide;
    private final Integer passengerId = 1;
    private final String from = "From";
    private final String to = "To";

    @PostConstruct
    public void setUp() {
        initClients();
    }

    private void initClients() {
        rideTestClient = new RideTestClient(restTemplate, RIDE.getHost(), RIDE.getFirstMappedPort());
        driverTestClient = new DriverTestClient(restTemplate, DRIVER.getHost(), DRIVER.getFirstMappedPort());
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
        driverTestClient.updateDriverStatus(driverId, "BUSY");
        waitingRide = rideTestClient.bookRide(passengerId, from, to);
        String rideId = waitingRide.id();

        ResponseEntity<ErrorResponse> response = rideTestClient.startRide(rideId, driverId, ErrorResponse.class);

        log.info("Response withBusyDriver: {}", response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @SneakyThrows
    void shouldStartRideAndEndRide() {
        Integer driverId = 2;
        waitingRide = rideTestClient.bookRide(passengerId, from, to);
        String rideId = waitingRide.id();
        driverTestClient.updateDriverStatus(driverId, "AVAILABLE");

        ResponseEntity<RideDto> startResponse = rideTestClient.startRide(rideId, driverId, RideDto.class);
        assertThat(startResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        Thread.sleep(2);

        ResponseEntity<RideDto> endResponse = rideTestClient.endRide(startResponse.getBody().id(), RideDto.class);
        assertThat(endResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        RideDto endBody = endResponse.getBody();
        assertThat(endBody.finishTime()).isNotNull();
    }
}
