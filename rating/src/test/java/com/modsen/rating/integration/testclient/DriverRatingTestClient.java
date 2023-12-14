package com.modsen.rating.integration.testclient;

import com.modsen.rating.dto.AvgRatingResponse;
import com.modsen.rating.dto.ScoreRequest;
import com.modsen.rating.exception.ErrorResponse;
import com.modsen.rating.exception.ValidationErrorResponse;
import com.modsen.rating.integration.util.HostUtil;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.springframework.http.HttpStatus;

public class DriverRatingTestClient {

    private final String BASE_URL;

    private DriverRatingTestClient(Integer port) {
        BASE_URL = HostUtil.getHost() + port + "/api/v1/ratings/drivers";
    }

    public static DriverRatingTestClient withPort(Integer port) {
        return new DriverRatingTestClient(port);
    }

    public AvgRatingResponse getAverageDriverRatingForOk(Integer driverId) {
        return RestAssured.when()
                .get(BASE_URL + "/avg/{driverId}", driverId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .extract()
                .as(AvgRatingResponse.class);
    }

    public ErrorResponse getAverageDriverRatingForNotFound(Integer driverId) {
        return RestAssured.when()
                .get(BASE_URL + "/avg/{driverId}", driverId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON)
                .extract()
                .as(ErrorResponse.class);
    }

    public void addScoreToDriverForCreated(ScoreRequest request) {
        RestAssured
                .given()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post(BASE_URL)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value());
    }

    public ValidationErrorResponse addScoreToDriverForBadRequest(ScoreRequest request) {
        return RestAssured
                .given()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post(BASE_URL)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ValidationErrorResponse.class);
    }

    public ValidatableResponse getDriverRatingsWithComments(Integer driverId) {
        return RestAssured
                .when()
                .get(BASE_URL + "/{driverId}", driverId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }
}
