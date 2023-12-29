package com.modsen.e2e.util;

import com.modsen.e2e.dto.DriverStatusUpdate;
import com.modsen.e2e.dto.RideDto;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class DriverTestClient {

    private static final String PATH_PREFIX = "/api/v1/drivers";
    private final String url;
    private final RestTemplate patchTemplate;

    public DriverTestClient(TestRestTemplate restTemplate, String host, Integer port) {
        this.patchTemplate = restTemplate.getRestTemplate();
        configurePatchTemplate();
        this.url = "http://" + host + ":" + port + PATH_PREFIX;
    }

    private void configurePatchTemplate() {
        HttpClient httpClient = HttpClientBuilder.create().build();
        this.patchTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    public void updateDriverStatus(Integer driverId, String status) {
        var response = patchTemplate.exchange(url + "/{id}", HttpMethod.PATCH,
                new HttpEntity<>(new DriverStatusUpdate(status)), RideDto.class, driverId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
