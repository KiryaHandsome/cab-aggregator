package com.modsen.driver.integration.testclient;

import com.modsen.driver.dto.request.DriverCreate;
import com.modsen.driver.dto.request.DriverUpdate;
import com.modsen.driver.util.HostUtil;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class DriverTestClient {

    private final String BASE_URL;

    private DriverTestClient(Integer port) {
        BASE_URL = HostUtil.getHost() + port + "/api/v1/drivers";
    }

    public static DriverTestClient withPort(Integer port) {
        return new DriverTestClient(port);
    }

    public ValidatableResponse getDriverById(Integer id) {
        return when()
                .get(BASE_URL + "/{id}", id)
                .then()
                .log().all();
    }

    public ValidatableResponse getDrivers(Integer pageNumber, Integer pageSize) {
        return given()
                .queryParam("page", pageNumber)
                .queryParam("size", pageSize)
                .when()
                .get(BASE_URL)
                .then()
                .log().all();
    }

    public ValidatableResponse updateDriver(Integer driverId, DriverUpdate requestBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .patch(BASE_URL + "/{id}", driverId)
                .then()
                .log().all();
    }

    public ValidatableResponse createDriver(DriverCreate requestBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(BASE_URL)
                .then()
                .log().all();
    }

    public ValidatableResponse deleteDriverById(Integer driverId) {
        return when()
                .delete(BASE_URL + "/{id}", driverId)
                .then()
                .log().all();
    }
}
