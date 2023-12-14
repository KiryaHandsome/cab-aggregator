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

public class PassengerRatingTestClient {

    private final String BASE_URL;

    private PassengerRatingTestClient(Integer port) {
        BASE_URL = HostUtil.getHost() + port + "/api/v1/ratings/passengers";
    }

    public static PassengerRatingTestClient withPort(Integer port) {
        return new PassengerRatingTestClient(port);
    }

    public AvgRatingResponse getAveragePassengerRatingForOk(Integer passengerId) {
        return RestAssured.when()
                .get(BASE_URL + "/avg/{passengerId}", passengerId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .extract()
                .as(AvgRatingResponse.class);
    }

    public ErrorResponse getAveragePassengerRatingForNotFound(Integer passengerId) {
        return RestAssured.when()
                .get(BASE_URL + "/avg/{passengerId}", passengerId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON)
                .extract()
                .as(ErrorResponse.class);
    }

    public void addScoreToPassengerForCreated(ScoreRequest request) {
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

    public ValidationErrorResponse addScoreToPassengerForBadRequest(ScoreRequest request) {
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

    public ValidatableResponse getPassengerRatingsWithComments(Integer passengerId) {
        return RestAssured
                .when()
                .get(BASE_URL + "/{passengerId}", passengerId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }
}
