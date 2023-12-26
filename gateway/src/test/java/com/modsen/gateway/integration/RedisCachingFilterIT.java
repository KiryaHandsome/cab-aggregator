package com.modsen.gateway.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.modsen.gateway.dto.PassengerResponse;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RedisCachingFilterIT extends RedisContainer {

    private static WireMockServer wireMockServer;
    private static PassengerTestClient passengerTestClient;

    @LocalServerPort
    private Integer port;

    @BeforeAll
    static void startServer() {
        wireMockServer = new WireMockServer(WireMockConfiguration
                .wireMockConfig()
                .dynamicPort()
        );
        wireMockServer.start();
    }

    @PostConstruct
    void initPassengerClient() {
        passengerTestClient = new PassengerTestClient("http://localhost:" + port);
    }

    @DynamicPropertySource
    static void init(DynamicPropertyRegistry registry) {
        registry.add("passenger.service.uri", wireMockServer::baseUrl);
    }

    @AfterAll
    static void stopServer() {
        wireMockServer.stop();
    }

    @Test
    void checkWireMockServer() {
        assertThat(wireMockServer.isRunning()).isTrue();
    }

    @Test
    @SneakyThrows
    void checkFilter_shouldCallOriginalMethodOnlyOnce() {
        int id = 1;
        String url = "/api/v1/passengers/" + id;
        PassengerResponse expected = new PassengerResponse(1, "name", "surname", "email", "phone");
        PassengerServiceWireMockConfigurer.configureGetPassenger(wireMockServer, url, expected);

        int requestCount = 3;
        List<PassengerResponse> responses = new ArrayList<>();
        for (int i = 0; i < requestCount; i++) {
            PassengerResponse response = passengerTestClient.getPassengerById(id);
            responses.add(response);
        }

        assertThat(responses).allMatch(expected::equals);
        wireMockServer.verify(exactly(1), getRequestedFor(urlEqualTo(url)));
    }

}