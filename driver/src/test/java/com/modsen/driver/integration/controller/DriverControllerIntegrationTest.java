package com.modsen.driver.integration.controller;

import com.modsen.driver.dto.request.DriverCreate;
import com.modsen.driver.dto.request.DriverUpdate;
import com.modsen.driver.dto.response.ErrorResponse;
import com.modsen.driver.integration.BaseIntegrationTest;
import com.modsen.driver.model.Driver;
import com.modsen.driver.model.Status;
import com.modsen.driver.util.HostUtil;
import com.modsen.driver.util.TestData;
import com.modsen.driver.util.TestEntities;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@Sql(
        scripts = {"classpath:sql/delete-all.sql", "classpath:sql/create-data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class DriverControllerIntegrationTest extends BaseIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @LocalServerPort
    private Integer port;

    @Test
    void getDriver_shouldReturnExpectedDriver() {
        Driver expected = TestEntities.johnDoe();

        when()
                .get(HostUtil.getHost() + port + "/api/v1/drivers/{id}", expected.getId())
                .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body("id", is(expected.getId()))
                .body("name", is(expected.getName()))
                .body("surname", is(expected.getSurname()))
                .body("email", is(expected.getEmail()))
                .body("phoneNumber", is(expected.getPhoneNumber()))
                .body("status", is(expected.getStatus().toString()))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void getDriver_shouldThrowDriverNotFoundException() {
        Integer driverId = 100;

        when()
                .get(HostUtil.getHost() + port + "/api/v1/drivers/{id}", driverId)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .body(notNullValue(ErrorResponse.class))
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getAllDrivers_shouldReturnPageOfDriversWithExpectedSizes() {
        Integer pageNumber = 0;
        Integer pageSize = 2;
        Integer totalElements = 3;

        given()
                .queryParam("page", pageNumber)
                .queryParam("size", pageSize)
                .when()
                .get(HostUtil.getHost() + port + "/api/v1/drivers")
                .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body("totalElements", is(totalElements))
                .body("numberOfElements", is(pageSize))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @SneakyThrows
    void updateDriver_shouldReturnUpdatedDriver() {
        DriverUpdate request = new DriverUpdate(
                TestData.NEW_NAME,
                null,
                TestData.NEW_EMAIL,
                null,
                null
        );
        Integer driverId = 1;

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .patch(HostUtil.getHost() + port + "/api/v1/drivers/{id}", driverId)
                .then()
                .log().all()
                .contentType(ContentType.JSON)
                .body("id", is(driverId))
                .body("name", is(TestData.NEW_NAME))
                .body("surname", is(TestEntities.JOHN_SURNAME))
                .body("email", is(TestData.NEW_EMAIL))
                .body("phoneNumber", is(TestEntities.JOHN_PHONE_NUMBER))
                .body("status", is(TestEntities.JOHN_STATUS.toString()))
                .statusCode(HttpStatus.OK.value());


    }

    @Test
    void updateDriver_shouldReturnNotFoundCode() {
        Integer driverId = 100;

        when()
                .get(HostUtil.getHost() + port + "/api/v1/drivers/{id}", driverId)
                .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body(notNullValue(ErrorResponse.class))
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @SneakyThrows
    void updateDriver_withExistingEmail_shouldReturnConflictStatus() {
        DriverUpdate request = new DriverUpdate(null, null, TestEntities.JOHN_EMAIL, null, null);
        Integer id = 2;

        given()
                .body(objectMapper.writeValueAsString(request))
                .contentType(ContentType.JSON)
                .when()
                .contentType(ContentType.JSON)
                .patch(HostUtil.getHost() + port + "/api/v1/drivers/{id}", id)
                .then()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT.value())
                .body(notNullValue(ErrorResponse.class));
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("invalidDriverUpdatesForBadRequest")
    void updateDriver_shouldReturnBadRequestStatus(DriverUpdate request) {
        Integer id = 2;

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .patch(HostUtil.getHost() + port + "/api/v1/drivers/{id}", 2)
                .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body("statusCode", is(HttpStatus.BAD_REQUEST.value()))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @SneakyThrows
    void createDriver_withExistingEmail_shouldReturnConflictStatus() {
        DriverCreate request = new DriverCreate(
                TestData.NAME,
                TestData.SURNAME,
                TestEntities.JOHN_EMAIL,
                TestData.NEW_PHONE_NUMBER
        );

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post(HostUtil.getHost() + port + "/api/v1/drivers")
                .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body("statusCode", is(HttpStatus.CONFLICT.value()))
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    @SneakyThrows
    void createDriver_withExistingPhoneNumber_shouldReturnConflictStatus() {
        DriverCreate request = new DriverCreate(
                TestData.NAME,
                TestData.SURNAME,
                TestData.NEW_EMAIL,
                TestEntities.JOHN_PHONE_NUMBER
        );

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post(HostUtil.getHost() + port + "/api/v1/drivers")
                .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body("statusCode", is(HttpStatus.CONFLICT.value()))
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @SneakyThrows
    @Test
    void createDriver_shouldReturnCreatedDriver() {
        DriverCreate request = TestData.newDriverCreate();
        Integer expectedId = 4;

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post(HostUtil.getHost() + port + "/api/v1/drivers")
                .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body("id", is(expectedId))
                .body("name", is(request.getName()))
                .body("surname", is(request.getSurname()))
                .body("email", is(request.getEmail()))
                .body("phoneNumber", is(request.getPhoneNumber()))
                .body("status", is(Status.OFFLINE.toString()))
                .statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, "/api/v1/drivers/" + expectedId);

    }

    @Test
    void deleteDriver_shouldReturnNoContentStatus() {
        Integer id = 1;

        when()
                .delete(HostUtil.getHost() + port + "/api/v1/drivers/{id}", id)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private static Stream<DriverUpdate> invalidDriverUpdatesForBadRequest() {
        return Stream.of(
                new DriverUpdate("", null, null, null, null), // invalid name
                new DriverUpdate(" ", null, null, null, null),// invalid name
                new DriverUpdate("a", null, null, null, null),// invalid name
                new DriverUpdate(null, "", null, null, null), // invalid surname
                new DriverUpdate(null, " ", null, null, null), // invalid surname
                new DriverUpdate(null, "a", null, null, null), // invalid surname
                new DriverUpdate(null, null, "", null, null), // invalid email
                new DriverUpdate(null, null, " ", null, null), // invalid email
                new DriverUpdate(null, null, "email", null, null), // invalid email
                new DriverUpdate(null, null, "email@", null, null), // invalid email
                new DriverUpdate(null, null, "email@com", null, null), // invalid email
                new DriverUpdate(null, null, null, "aaa", null), // invalid phone
                new DriverUpdate(null, null, null, "", null), // invalid phone
                new DriverUpdate(null, null, null, " ", null), // invalid phone
                new DriverUpdate(null, null, null, "21381914", null), // invalid phone
                new DriverUpdate(null, null, null, "+1234567890123", null) // invalid phone
        );
    }
}