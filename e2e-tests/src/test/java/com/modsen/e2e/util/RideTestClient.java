package com.modsen.e2e.util;

import com.modsen.e2e.dto.RideRequest;
import com.modsen.e2e.dto.RideStart;
import com.modsen.e2e.dto.WaitingRideResponse;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class RideTestClient {

    private final TestRestTemplate restTemplate;
    private final String url;
    private static final String PATH_PREFIX = "/api/v1/rides";

    public RideTestClient(TestRestTemplate restTemplate, String host, Integer port) {
        this.restTemplate = restTemplate;
        this.url = "http://" + host + ":" + port;
    }

    public <T> ResponseEntity<T> startRide(String rideId, Integer driverId, Class<T> type) {
        return restTemplate.postForEntity(url + PATH_PREFIX + "/{id}/start",
                new RideStart(driverId), type, rideId);
    }

    public <T> ResponseEntity<T> endRide(String rideId, Class<T> type) {
        return restTemplate.postForEntity(url + PATH_PREFIX + "/{id}/end", null, type, rideId);
    }

    public WaitingRideResponse bookRide(Integer passengerId, String from, String to) {
        var response = restTemplate.postForEntity(url + PATH_PREFIX + "/book",
                new RideRequest(passengerId, from, to), WaitingRideResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }
}
