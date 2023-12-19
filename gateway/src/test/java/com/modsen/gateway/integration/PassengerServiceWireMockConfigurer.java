package com.modsen.gateway.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.Body;
import com.modsen.gateway.dto.PassengerResponse;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;

public class PassengerServiceWireMockConfigurer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static void configureGetPassenger(WireMockServer server, String url, PassengerResponse responseBody) {
        byte[] responseBodyJsonData = objectMapper.writeValueAsBytes(responseBody);
        server.stubFor(get(url)
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withResponseBody(Body.fromJsonBytes(responseBodyJsonData))
                )
        );
    }
}
