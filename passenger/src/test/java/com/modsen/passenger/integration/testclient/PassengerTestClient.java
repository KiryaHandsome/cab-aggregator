package com.modsen.passenger.integration.testclient;

import com.modsen.passenger.dto.request.PassengerCreate;
import com.modsen.passenger.dto.request.PassengerUpdate;
import com.modsen.passenger.util.HostUtil;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class PassengerTestClient {

    private final String BASE_URL;

    private PassengerTestClient(Integer port) {
        BASE_URL = HostUtil.getHost() + port + "/api/v1/passengers";
    }

    public static PassengerTestClient withPort(Integer port) {
        return new PassengerTestClient(port);
    }

    public ValidatableResponse getPassengerById(Integer id) {
        return when()
                .get(BASE_URL + "/{id}", id)
                .then()
                .log().all();
    }

    public ValidatableResponse getPassengers(Integer pageNumber, Integer pageSize) {
        return given()
                .queryParam("page", pageNumber)
                .queryParam("size", pageSize)
                .when()
                .get(BASE_URL)
                .then()
                .log().all();
    }

    public ValidatableResponse updatePassenger(Integer driverId, PassengerUpdate requestBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .patch(BASE_URL + "/{id}", driverId)
                .then()
                .log().all();
    }

    public ValidatableResponse createPassenger(PassengerCreate requestBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(BASE_URL)
                .then()
                .log().all();
    }

    public ValidatableResponse deletePassengerById(Integer driverId) {
        return when()
                .delete(BASE_URL + "/{id}", driverId)
                .then()
                .log().all();
    }
}
