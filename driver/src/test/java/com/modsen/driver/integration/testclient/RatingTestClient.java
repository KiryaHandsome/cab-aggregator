package com.modsen.driver.integration.testclient;

import com.modsen.driver.dto.request.ScoreRequest;
import com.modsen.driver.dto.response.RatingResponse;
import com.modsen.driver.dto.response.ValidationErrorResponse;
import com.modsen.driver.util.HostUtil;
import io.restassured.http.ContentType;
import org.springframework.http.HttpStatus;

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

    public RatingResponse getRating(Integer driverId) {
        return when()
                .get(BASE_URL + "/{id}", driverId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(RatingResponse.class);
    }

    public RatingResponse addScore(Integer driverId, ScoreRequest scoreRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(scoreRequest)
                .when()
                .post(BASE_URL + "/{id}", driverId)
                .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(RatingResponse.class);
    }

    public ValidationErrorResponse addScoreForValidationError(Integer driverId, ScoreRequest scoreRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(scoreRequest)
                .when()
                .post(BASE_URL + "/{id}", driverId)
                .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ValidationErrorResponse.class);
    }
}
