package com.modsen.driver.integration.controller;

import com.modsen.driver.dto.request.DriverCreate;
import com.modsen.driver.dto.request.DriverUpdate;
import com.modsen.driver.dto.response.DriverResponse;
import com.modsen.driver.dto.response.ErrorResponse;
import com.modsen.driver.dto.response.ValidationErrorResponse;
import com.modsen.driver.integration.BaseIntegrationTest;
import com.modsen.driver.integration.testclient.DriverTestClient;
import com.modsen.driver.mapper.DriverMapper;
import com.modsen.driver.repository.DriverRepository;
import com.modsen.driver.util.TestData;
import com.modsen.driver.util.TestEntities;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

@Sql(
        scripts = {"classpath:sql/delete-all.sql", "classpath:sql/create-data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class DriverControllerIT extends BaseIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private DriverMapper driverMapper;

    @Autowired
    private DriverRepository driverRepository;
    private DriverTestClient driverTestClient;

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

    // before all but after beans are injected
    @PostConstruct
    public void setUp() {
        driverTestClient = DriverTestClient.withPort(port);
    }

    @Test
    void getDriver_shouldReturnExpectedDriver() {
        int driverId = TestEntities.JOHN_ID;
        DriverResponse expected = driverMapper.toResponse(TestEntities.johnDoe());

        DriverResponse actual = driverTestClient.getDriverById(driverId)
                .assertThat()
                .contentType(ContentType.JSON)
                .extract()
                .as(DriverResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getDriver_shouldReturnNotFoundCode() {
        int driverId = 100;

        ErrorResponse actual = driverTestClient.getDriverById(driverId)
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getAllDrivers_shouldReturnPageOfDriversWithExpectedSizes() {
        int pageNumber = 0;
        int pageSize = 2;
        int totalElements = 3;

        driverTestClient.getDrivers(pageNumber, pageSize)
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value())
                .body("totalElements", is(totalElements));
    }

    @Test
    @SneakyThrows
    void updateDriver_shouldReturnUpdatedDriver() {
        DriverUpdate requestBody = new DriverUpdate(
                TestData.NEW_NAME,
                null,
                TestData.NEW_EMAIL,
                null,
                null
        );
        DriverResponse expected = new DriverResponse(
                TestEntities.JOHN_ID,
                TestData.NEW_NAME,
                TestEntities.JOHN_SURNAME,
                TestData.NEW_EMAIL,
                TestEntities.JOHN_PHONE_NUMBER,
                TestEntities.JOHN_STATUS
        );
        int driverId = TestEntities.JOHN_ID;

        DriverResponse actual = driverTestClient.updateDriver(driverId, requestBody)
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(new TypeRef<>() {
                });

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateDriver_shouldReturnNotFoundCode() {
        int driverId = 100;

        ErrorResponse actual = driverTestClient.updateDriver(driverId, new DriverUpdate())
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON)
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @SneakyThrows
    void updateDriver_withExistingEmail_shouldReturnConflictStatus() {
        int driverId = 2;
        DriverUpdate requestBody = new DriverUpdate(null, null, TestEntities.JOHN_EMAIL, null, null);

        ErrorResponse actual = driverTestClient.updateDriver(driverId, requestBody)
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("invalidDriverUpdatesForBadRequest")
    void updateDriver_shouldReturnBadRequestStatus(DriverUpdate requestBody) {
        int driverId = 2;

        ValidationErrorResponse actual = driverTestClient.updateDriver(driverId, requestBody)
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ValidationErrorResponse.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @SneakyThrows
    void createDriver_withExistingEmail_shouldReturnConflictStatus() {
        DriverCreate requestBody = new DriverCreate(
                TestData.NAME,
                TestData.SURNAME,
                TestEntities.JOHN_EMAIL,
                TestData.NEW_PHONE_NUMBER
        );

        ErrorResponse actual = driverTestClient.createDriver(requestBody)
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    @SneakyThrows
    void createDriver_withExistingPhoneNumber_shouldReturnConflictStatus() {
        DriverCreate requestBody = new DriverCreate(
                TestData.NAME,
                TestData.SURNAME,
                TestData.NEW_EMAIL,
                TestEntities.JOHN_PHONE_NUMBER
        );

        ErrorResponse actual = driverTestClient.createDriver(requestBody)
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.CONFLICT.value())
                .extract()
                .as(ErrorResponse.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    @SneakyThrows
    void createDriver_shouldReturnCreatedDriver() {
        DriverCreate requestBody = TestData.newDriverCreate();
        int expectedId = 4;
        DriverResponse expected = new DriverResponse(
                expectedId,
                TestData.NEW_NAME,
                TestData.NEW_SURNAME,
                TestData.NEW_EMAIL,
                TestData.NEW_PHONE_NUMBER,
                TestData.STATUS
        );

        DriverResponse actual = driverTestClient.createDriver(requestBody)
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, "/api/v1/drivers/" + expectedId)
                .extract()
                .as(DriverResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteDriver_shouldReturnNoContentStatus() {
        int driverId = 1;

        driverTestClient.deleteDriverById(driverId)
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(driverRepository.findById(driverId)).isNotPresent();
    }
}