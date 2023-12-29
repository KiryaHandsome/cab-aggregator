package com.modsen.passenger.integration.controller;

import com.modsen.passenger.dto.request.PassengerCreate;
import com.modsen.passenger.dto.request.PassengerUpdate;
import com.modsen.passenger.dto.response.ErrorResponse;
import com.modsen.passenger.dto.response.PassengerResponse;
import com.modsen.passenger.dto.response.ValidationErrorResponse;
import com.modsen.passenger.integration.PostgresContainer;
import com.modsen.passenger.integration.testclient.PassengerTestClient;
import com.modsen.passenger.mapper.PassengerMapper;
import com.modsen.passenger.repository.PassengerRepository;
import com.modsen.passenger.util.TestConstants;
import com.modsen.passenger.util.TestData;
import com.modsen.passenger.util.TestEntities;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
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
        scripts = {"classpath:" + TestConstants.DELETE_SCRIPT_PATH, "classpath:" + TestConstants.CREATE_SCRIPT_PATH},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class PassengerControllerIT extends PostgresContainer {

    @LocalServerPort
    private Integer port;

    @Autowired
    private PassengerMapper passengerMapper;

    @Autowired
    private PassengerRepository passengerRepository;
    private PassengerTestClient passengerTestClient;

    // before all but after beans are injected

    @PostConstruct
    public void setUp() {
        passengerTestClient = PassengerTestClient.withPort(port);
    }

    @Test
    void getPassenger_shouldReturnExpectedPassenger() {
        int passengerId = TestEntities.JOHN_ID;
        PassengerResponse expected = passengerMapper.toResponse(TestEntities.johnDoe());

        PassengerResponse actual = passengerTestClient.getPassengerById(passengerId);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getPassenger_shouldReturnNotFoundCode() {
        int passengerId = 100;

        ErrorResponse actual = passengerTestClient.getPassengerByIdForError(passengerId);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getAllPassengers_shouldReturnPageOfPassengersWithExpectedSizes() {
        int pageNumber = 0;
        int pageSize = 2;
        int totalElements = 3;

        passengerTestClient.getPassengers(pageNumber, pageSize)
                .body("totalElements", is(totalElements));
    }

    @Test
    @SneakyThrows
    void updatePassenger_shouldReturnUpdatedPassenger() {
        PassengerUpdate requestBody = new PassengerUpdate(
                TestData.NEW_NAME,
                null,
                TestData.NEW_EMAIL,
                null
        );
        PassengerResponse expected = new PassengerResponse(
                TestEntities.JOHN_ID,
                TestData.NEW_NAME,
                TestEntities.JOHN_SURNAME,
                TestData.NEW_EMAIL,
                TestEntities.JOHN_PHONE_NUMBER
        );
        int passengerId = TestEntities.JOHN_ID;

        PassengerResponse actual = passengerTestClient.updatePassenger(passengerId, requestBody);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updatePassenger_shouldReturnNotFoundCode() {
        int passengerId = 100;

        ErrorResponse actual = passengerTestClient.updatePassengerForNotFound(passengerId, new PassengerUpdate());

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @SneakyThrows
    void updatePassenger_withExistingEmail_shouldReturnConflictStatus() {
        int passengerId = 2;
        PassengerUpdate requestBody = new PassengerUpdate(null, null, TestEntities.JOHN_EMAIL, null);

        ErrorResponse actual = passengerTestClient.updatePassengerForConflict(passengerId, requestBody);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("invalidPassengerUpdatesForBadRequest")
    void updatePassenger_shouldReturnBadRequestStatus(PassengerUpdate requestBody) {
        int passengerId = 2;

        ValidationErrorResponse actual = passengerTestClient
                .updatePassengerForValidationError(passengerId, requestBody);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @SneakyThrows
    void createPassenger_withExistingEmail_shouldReturnConflictStatus() {
        PassengerCreate requestBody = new PassengerCreate(
                TestData.NAME,
                TestData.SURNAME,
                TestEntities.JOHN_EMAIL,
                TestData.NEW_PHONE_NUMBER
        );

        ErrorResponse actual = passengerTestClient.createPassengerForConflict(requestBody);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    @SneakyThrows
    void createPassenger_withExistingPhoneNumber_shouldReturnConflictStatus() {
        PassengerCreate requestBody = new PassengerCreate(
                TestData.NAME,
                TestData.SURNAME,
                TestData.NEW_EMAIL,
                TestEntities.JOHN_PHONE_NUMBER
        );

        ErrorResponse actual = passengerTestClient.createPassengerForConflict(requestBody);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    @SneakyThrows
    void createPassenger_shouldReturnCreatedPassenger() {
        PassengerCreate requestBody = TestData.newPassengerCreate();
        int expectedId = 4;
        PassengerResponse expected = new PassengerResponse(
                expectedId,
                TestData.NEW_NAME,
                TestData.NEW_SURNAME,
                TestData.NEW_EMAIL,
                TestData.NEW_PHONE_NUMBER
        );

        PassengerResponse actual = passengerTestClient.createPassenger(expectedId, requestBody);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deletePassenger_shouldReturnNoContentStatus() {
        int passengerId = 1;

        passengerTestClient.deletePassengerById(passengerId)
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(passengerRepository.findById(passengerId)).isNotPresent();
    }

    private static Stream<PassengerUpdate> invalidPassengerUpdatesForBadRequest() {
        return Stream.of(
                new PassengerUpdate("", null, null, null), // invalid name
                new PassengerUpdate(" ", null, null, null),// invalid name
                new PassengerUpdate("a", null, null, null),// invalid name
                new PassengerUpdate(null, "", null, null), // invalid surname
                new PassengerUpdate(null, " ", null, null), // invalid surname
                new PassengerUpdate(null, "a", null, null), // invalid surname
                new PassengerUpdate(null, null, "", null), // invalid email
                new PassengerUpdate(null, null, " ", null), // invalid email
                new PassengerUpdate(null, null, "email", null), // invalid email
                new PassengerUpdate(null, null, "email@", null), // invalid email
                new PassengerUpdate(null, null, "email@com", null), // invalid email
                new PassengerUpdate(null, null, null, "aaa"), // invalid phone
                new PassengerUpdate(null, null, null, ""), // invalid phone
                new PassengerUpdate(null, null, null, " "), // invalid phone
                new PassengerUpdate(null, null, null, "21381914"), // invalid phone
                new PassengerUpdate(null, null, null, "+1234567890123") // invalid phone
        );
    }
}