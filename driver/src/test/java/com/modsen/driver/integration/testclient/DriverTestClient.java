package com.modsen.driver.integration.testclient;

import com.modsen.driver.dto.request.DriverCreate;
import com.modsen.driver.dto.request.DriverUpdate;
import com.modsen.driver.dto.response.DriverResponse;
import com.modsen.driver.dto.response.ErrorResponse;
import com.modsen.driver.dto.response.ValidationErrorResponse;
import com.modsen.driver.util.HostUtil;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;

public class DriverTestClient {

    private final String BASE_URL;

    private DriverTestClient(Integer port) {
        BASE_URL = HostUtil.getHost() + port + "/api/v1/drivers";
    }

    public static DriverTestClient withPort(Integer port) {
        return new DriverTestClient(port);
    }

    public DriverResponse getDriverById(Integer id) {
        return when()
                .get(BASE_URL + "/{id}", id)
                .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .extract()
                .as(DriverResponse.class);
    }

    public ErrorResponse getDriverByIdForNotFound(Integer id) {
        return when()
                .get(BASE_URL + "/{id}", id)
                .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponse.class);
    }

    public ValidatableResponse getDrivers(Integer pageNumber, Integer pageSize, Integer totalElements) {
        return given()
                .queryParam("page", pageNumber)
                .queryParam("size", pageSize)
                .when()
                .get(BASE_URL)
                .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value())
                .body("totalElements", is(totalElements));
    }

    public DriverResponse updateDriver(Integer driverId, DriverUpdate requestBody) {
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
                .as(DriverResponse.class);
    }

    public ErrorResponse updateDriverForConflict(Integer driverId, DriverUpdate requestBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .patch(BASE_URL + "/{id}", driverId)
                .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .as(ErrorResponse.class);
    }

    public ValidationErrorResponse updateDriverForValidationError(Integer driverId, DriverUpdate requestBody) {
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

    public ErrorResponse updateDriverForNotFound(Integer driverId, DriverUpdate requestBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .patch(BASE_URL + "/{id}", driverId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON)
                .extract()
                .as(ErrorResponse.class);
    }

    public DriverResponse createDriver(Integer expectedId, DriverCreate requestBody) {
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
                .header(HttpHeaders.LOCATION, "/api/v1/drivers/" + expectedId)
                .extract()
                .as(DriverResponse.class);
    }

    public ErrorResponse createDriverForConflict(DriverCreate requestBody) {
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

    public ValidatableResponse deleteDriverById(Integer driverId) {
        return when()
                .delete(BASE_URL + "/{id}", driverId)
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
