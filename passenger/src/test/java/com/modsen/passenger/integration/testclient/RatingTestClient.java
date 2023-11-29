package com.modsen.passenger.integration.testclient;

import com.modsen.passenger.dto.request.ScoreRequest;
import com.modsen.passenger.util.HostUtil;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class RatingTestClient {

    private final String BASE_URL;

    private RatingTestClient(Integer port) {
        BASE_URL = HostUtil.getHost() + port + "/api/v1/ratings";
    }

    public static RatingTestClient withPort(Integer port) {
        return new RatingTestClient(port);
    }

    public ValidatableResponse getRating(Integer driverId) {
        return when()
                .get(BASE_URL + "/{id}", driverId)
                .then()
                .log().all();
    }

    public ValidatableResponse addScore(Integer driverId, ScoreRequest scoreRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(scoreRequest)
                .when()
                .post(BASE_URL + "/{id}", driverId)
                .then()
                .log().all();
    }
}
