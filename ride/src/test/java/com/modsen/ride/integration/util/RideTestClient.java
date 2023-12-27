package com.modsen.ride.integration.util;

import com.modsen.ride.dto.request.RideRequest;
import com.modsen.ride.dto.request.RideStart;
import com.modsen.ride.dto.response.WaitingRideResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class RideTestClient {

    private static final String PATH_PREFIX = "/api/v1/rides";

    private final TestRestTemplate restTemplate;

    public WaitingRideResponse bookRide(Integer passengerId, String from, String to) {
        ResponseEntity<WaitingRideResponse> response = restTemplate.postForEntity(PATH_PREFIX + "/book",
                new RideRequest(passengerId, from, to), WaitingRideResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(response.getStatusCode());
        return response.getBody();
    }

    public <T> ResponseEntity<T> startRide(String waitingRideId, Integer driverId, Class<T> responseType) {
        String urlTemplate = PATH_PREFIX + "/%s/start";
        return restTemplate.postForEntity(String.format(urlTemplate, waitingRideId),
                new RideStart(driverId), responseType);
    }

}
