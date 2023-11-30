package com.modsen.passenger.integration.testclient;

import com.modsen.passenger.dto.request.PassengerCreate;
import com.modsen.passenger.dto.request.PassengerUpdate;
import com.modsen.passenger.dto.response.ErrorResponse;
import com.modsen.passenger.dto.response.PassengerResponse;
import com.modsen.passenger.dto.response.ValidationErrorResponse;
import com.modsen.passenger.util.HostUtil;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;

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

    public PassengerResponse getPassengerById(Integer id) {
        return when()
                .get(BASE_URL + "/{id}", id)
                .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .extract()
                .as(PassengerResponse.class);
    }

    public ValidatableResponse getPassengers(Integer pageNumber, Integer pageSize) {
        return given()
                .queryParam("page", pageNumber)
                .queryParam("size", pageSize)
                .when()
                .get(BASE_URL)
                .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value());
    }

    public PassengerResponse updatePassenger(Integer driverId, PassengerUpdate requestBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .patch(BASE_URL + "/{id}", driverId)
                .then()
                .log().all()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(new TypeRef<>() {
                });
    }

    public ValidationErrorResponse updatePassengerForValidationError(Integer driverId, PassengerUpdate requestBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .patch(BASE_URL + "/{id}", driverId)
                .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ValidationErrorResponse.class);
    }

    public PassengerResponse createPassenger(Integer expectedId, PassengerCreate requestBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(BASE_URL)
                .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, "/api/v1/passengers/" + expectedId)
                .extract()
                .as(PassengerResponse.class);
    }

    public ErrorResponse createPassengerForConflict(PassengerCreate requestBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(BASE_URL)
                .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .as(ErrorResponse.class);
    }

    public ValidatableResponse deletePassengerById(Integer driverId) {
        return when()
                .delete(BASE_URL + "/{id}", driverId)
                .then()
                .log().all();
    }

    public ErrorResponse getPassengerByIdForError(int passengerId) {
        return when()
                .get(BASE_URL + "/{id}", passengerId)
                .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponse.class);
    }

    public ErrorResponse updatePassengerForNotFound(int passengerId, PassengerUpdate requestBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .patch(BASE_URL + "/{id}", passengerId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON)
                .extract()
                .as(ErrorResponse.class);
    }

    public ErrorResponse updatePassengerForConflict(int passengerId, PassengerUpdate requestBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .patch(BASE_URL + "/{id}", passengerId)
                .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .as(ErrorResponse.class);
    }
}
