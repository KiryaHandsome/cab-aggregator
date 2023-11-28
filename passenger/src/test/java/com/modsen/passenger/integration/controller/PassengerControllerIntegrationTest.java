package com.modsen.passenger.integration.controller;


import com.modsen.passenger.dto.request.PassengerCreate;
import com.modsen.passenger.dto.request.PassengerUpdate;
import com.modsen.passenger.dto.response.ErrorResponse;
import com.modsen.passenger.integration.BaseIntegrationTest;
import com.modsen.passenger.model.Passenger;
import com.modsen.passenger.repository.PassengerRepository;
import com.modsen.passenger.util.HostUtil;
import com.modsen.passenger.util.TestData;
import com.modsen.passenger.util.TestEntities;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@Sql(
        scripts = {"classpath:sql/delete-all.sql", "classpath:sql/create-data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class PassengerControllerIntegrationTest extends BaseIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @LocalServerPort
    private Integer port;

    @Autowired
    private PassengerRepository passengerRepository;

    //@formatter:off
    @Test
    void getById_shouldReturnExpectedPassenger() {
        Passenger expected = TestEntities.johnDoe();

        when()
                .get(HostUtil.getHost() + port + "/api/v1/passengers/{id}", expected.getId())
        .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body("id", is(expected.getId()))
                .body("name", is(expected.getName()))
                .body("surname", is(expected.getSurname()))
                .body("email", is(expected.getEmail()))
                .body("phoneNumber", is(expected.getPhoneNumber()))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void getPassengerById_shouldReturnStatusNotFoundAndNotEmptyMessage() {
        when()
                .get(HostUtil.getHost() + port + "/api/v1/passengers/{id}", Integer.MAX_VALUE)
        .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body(notNullValue(ErrorResponse.class))
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getAllPassengers_shouldReturnPageWithExpectedSize() {
        Integer pageNumber = 0;
        Integer pageSize = 2;
        Integer totalElement = 3;

        given()
                .queryParam("page", pageNumber)
                .queryParam("size", pageSize)
        .when()
                .get(HostUtil.getHost() + port + "/api/v1/passengers")
        .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body("totalElements", is(totalElement))
                .body("numberOfElements", is(pageSize))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @SneakyThrows
    void updatePassenger_shouldReturnUpdatedNameAndEmail() {
        PassengerUpdate request = new PassengerUpdate(
                TestData.NEW_NAME,
                null,
                TestData.NEW_EMAIL,
                null
        );
        Integer passengerId = TestEntities.JOHN_ID;

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .patch(HostUtil.getHost() + port + "/api/v1/passengers/{id}", passengerId)
        .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body("id", is(passengerId))
                .body("name", is(TestData.NEW_NAME))
                .body("surname", is(TestEntities.JOHN_SURNAME))
                .body("email", is(TestData.NEW_EMAIL))
                .body("phoneNumber", is(TestEntities.JOHN_PHONE_NUMBER))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @SneakyThrows
    void updatePassenger_shouldReturnUpdatedSurnameAndPhoneNumber() {
        PassengerUpdate request = new PassengerUpdate(
                null,
                TestData.NEW_SURNAME,
                null,
                TestData.NEW_PHONE_NUMBER
        );
        Integer passengerId = TestEntities.JOHN_ID;

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .patch(HostUtil.getHost() + port + "/api/v1/passengers/{id}", passengerId)
        .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body("id", is(passengerId))
                .body("name", is(TestEntities.JOHN_NAME))
                .body("surname", is(TestData.NEW_SURNAME))
                .body("email", is(TestEntities.JOHN_EMAIL))
                .body("phoneNumber", is(TestData.NEW_PHONE_NUMBER))
                .statusCode(HttpStatus.OK.value());
    }

    @ParameterizedTest
    @SneakyThrows
    @MethodSource("invalidPassengerUpdates")
    void updatePassenger_shouldReturnBadRequest(PassengerUpdate request) {
        Integer passengerId = TestEntities.JOHN_ID;

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .patch(HostUtil.getHost() + port + "/api/v1/passengers/{id}", passengerId)
                .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body("statusCode", is(HttpStatus.BAD_REQUEST.value()))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @SneakyThrows
    void updatePassenger_withExistingEmail_shouldReturnConflict() {
        PassengerCreate request = TestData.newPassengerCreate();
        request.setEmail(TestEntities.JOHN_EMAIL);
        Integer id = 2;

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .patch(HostUtil.getHost() + port + "/api/v1/passengers/{id}", id)
        .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body("statusCode", is(HttpStatus.CONFLICT.value()))
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    @SneakyThrows
    void updatePassenger_withExistingPhoneNumber_shouldReturnConflict() {
        PassengerUpdate request = TestData.newPassengerUpdate();
        request.setPhoneNumber(TestEntities.JOHN_PHONE_NUMBER);
        Integer id = 2;

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .patch(HostUtil.getHost() + port + "/api/v1/passengers/{id}", id)
        .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body("statusCode", is(HttpStatus.CONFLICT.value()))
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    @SneakyThrows
    void createPassenger_shouldReturnCreatedPassenger() {
        PassengerCreate request = TestData.defaultPassengerCreate();
        Integer expectedId = 4;

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post(HostUtil.getHost() + port + "/api/v1/passengers")
        .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body("id", is(expectedId))
                .body("name", is(request.getName()))
                .body("surname", is(request.getSurname()))
                .body("email", is(request.getEmail()))
                .body("phoneNumber", is(request.getPhoneNumber()))
                .header(HttpHeaders.LOCATION, is("/api/v1/passengers/" + expectedId))
                .statusCode(HttpStatus.CREATED.value());
    }

    @ParameterizedTest
    @SneakyThrows
    @MethodSource("invalidPassengerCreatesForBadRequest")
    void createPassenger_shouldReturnBadRequest(PassengerCreate request) {
        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post(HostUtil.getHost() + port + "/api/v1/passengers")
        .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body("statusCode", is(HttpStatus.BAD_REQUEST.value()))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @SneakyThrows
    void createPassenger_withExistingEmail_shouldReturnConflict() {
        PassengerCreate request = TestData.newPassengerCreate();
        request.setEmail(TestEntities.JOHN_EMAIL);

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post(HostUtil.getHost() + port + "/api/v1/passengers")
        .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body("statusCode", is(HttpStatus.CONFLICT.value()))
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    @SneakyThrows
    void createPassenger_withExistingPhoneNumber_shouldReturnConflict() {
        PassengerCreate request = TestData.newPassengerCreate();
        request.setPhoneNumber(TestEntities.JOHN_PHONE_NUMBER);

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post(HostUtil.getHost() + port + "/api/v1/passengers")
        .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body("statusCode", is(HttpStatus.CONFLICT.value()))
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void deletePassenger_shouldReturnNoContent() {
        Integer id = 1;

        when()
                .delete(HostUtil.getHost() + port + "/api/v1/passengers/{id}", id)
        .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(passengerRepository.findById(id)).isNotPresent();
    }

    private static Stream<PassengerCreate> invalidPassengerCreatesForBadRequest() {
        return Stream.of(
                new PassengerCreate("", TestData.NEW_SURNAME, TestData.NEW_EMAIL, TestData.NEW_PHONE_NUMBER), // invalid name
                new PassengerCreate("   ", TestData.NEW_SURNAME, TestData.NEW_EMAIL, TestData.NEW_PHONE_NUMBER), // invalid name
                new PassengerCreate("a", TestData.NEW_SURNAME, TestData.NEW_EMAIL, TestData.NEW_PHONE_NUMBER), // invalid name
                new PassengerCreate(TestData.NEW_NAME, "", TestData.NEW_EMAIL, TestData.NEW_PHONE_NUMBER), // invalid surname
                new PassengerCreate(TestData.NEW_NAME, "    ", TestData.NEW_EMAIL, TestData.NEW_PHONE_NUMBER), // invalid surname
                new PassengerCreate(TestData.NEW_NAME, "b", TestData.NEW_EMAIL, TestData.NEW_PHONE_NUMBER), // invalid surname
                new PassengerCreate(TestData.NEW_NAME, TestData.NEW_SURNAME, "email", TestData.NEW_PHONE_NUMBER), // invalid email
                new PassengerCreate(TestData.NEW_NAME, TestData.NEW_SURNAME, "email@", TestData.NEW_PHONE_NUMBER), // invalid email
                new PassengerCreate(TestData.NEW_NAME, TestData.NEW_SURNAME, "email@com", TestData.NEW_PHONE_NUMBER), // invalid email
                new PassengerCreate(TestData.NEW_NAME, TestData.NEW_SURNAME, TestData.NEW_EMAIL, "asfd"), // invalid phone
                new PassengerCreate(TestData.NEW_NAME, TestData.NEW_SURNAME, TestData.NEW_EMAIL, "123asf"), // invalid phone
                new PassengerCreate(TestData.NEW_NAME, TestData.NEW_SURNAME, TestData.NEW_EMAIL, "+123asf"), // invalid phone
                new PassengerCreate(TestData.NEW_NAME, TestData.NEW_SURNAME, TestData.NEW_EMAIL, " ") // invalid phone
        );
    }

    private static Stream<PassengerUpdate> invalidPassengerUpdates() {
        return Stream.of(
                new PassengerUpdate(null, null, null, ""), // invalid number
                new PassengerUpdate(null, null, null, "123456"), // invalid number
                new PassengerUpdate(null, null, null, "abc"), // invalid number
                new PassengerUpdate("", null, null, null), // invalid name
                new PassengerUpdate(" ", null, null, null), // invalid name
                new PassengerUpdate("a", null, null, null), // invalid name
                new PassengerUpdate(null, "", null, null), // invalid surname
                new PassengerUpdate(null, " ", null, null), // invalid surname
                new PassengerUpdate(null, "a", null, null), // invalid surname
                new PassengerUpdate(null, null, "email", null), // invalid email
                new PassengerUpdate(null, null, "email@", null), // invalid email
                new PassengerUpdate(null, null, " ", null), // invalid email
                new PassengerUpdate(null, null, "email@com", null) // invalid email
        );
    }
}