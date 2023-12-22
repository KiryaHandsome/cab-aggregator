package com.modsen.e2etests;

import com.modsen.e2etests.util.E2ESuite;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;


@Slf4j
public class StartRideTest extends E2ESuite {

    private TestRestTemplate restTemplate;

    @Test
    void shouldNotStartRide() {
        String url = RIDE.getHost() + "/api/v1/rides";
        log.info("Ride service url: {}", url);
//        restTemplate.getForEntity(url)
//        restTemplate.postForEntity()
    }
}
