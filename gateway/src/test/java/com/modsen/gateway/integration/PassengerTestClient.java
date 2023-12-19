package com.modsen.gateway.integration;

import com.modsen.gateway.dto.PassengerResponse;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.when;

public class PassengerTestClient {

    private final String BASE_URL;

    public PassengerTestClient(String baseUrl) {
        BASE_URL = baseUrl + "/api/v1/passengers";
    }

    public PassengerResponse getPassengerById(Integer id) {
        return when()
                .get(BASE_URL + "/{id}", id)
                .then()
                .log().ifValidationFails()
                .assertThat()
                .contentType(ContentType.JSON)
                .extract()
                .as(PassengerResponse.class);
    }
}
