package com.modsen.rating.integration.controller;

import com.modsen.rating.dto.AvgRatingResponse;
import com.modsen.rating.dto.ScoreRequest;
import com.modsen.rating.exception.ErrorResponse;
import com.modsen.rating.exception.ValidationErrorResponse;
import com.modsen.rating.integration.BaseIntegrationTest;
import com.modsen.rating.integration.testclient.PassengerRatingTestClient;
import com.modsen.rating.model.PassengerRating;
import com.modsen.rating.repository.PassengerRatingRepository;
import com.modsen.rating.util.TestData;
import jakarta.annotation.PostConstruct;
import org.assertj.core.data.Offset;
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
        scripts = {"classpath:sql/delete-data.sql", "classpath:sql/create-data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PassengerControllerIT extends BaseIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private PassengerRatingRepository passengerRatingRepository;
    private PassengerRatingTestClient testClient;

    @PostConstruct
    void setUp() {
        testClient = PassengerRatingTestClient.withPort(port);
    }

    @Test
    void getAveragePassengerRating_shouldReturnExpectedAvgRating() {
        int passengerId = 1;
        float expectedAvgRating = 3f;

        AvgRatingResponse actual = testClient.getAveragePassengerRatingForOk(passengerId);

        assertThat(actual.getAverageRating()).isCloseTo(expectedAvgRating, Offset.offset(0.01f));
    }

    @Test
    void getAveragePassengerRating_shouldReturnNotFoundStatus() {
        int passengerId = 100;

        ErrorResponse actual = testClient.getAveragePassengerRatingForNotFound(passengerId);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void addScoreToPassenger_shouldReturnCreatedStatusAndSaveRatingToDb() {
        int expectedId = 4;
        PassengerRating expected = new PassengerRating(expectedId, TestData.DRIVER_ID, TestData.SCORE, TestData.COMMENT);
        ScoreRequest requestBody = new ScoreRequest(TestData.DRIVER_ID, TestData.SCORE, TestData.COMMENT);

        testClient.addScoreToPassengerForCreated(requestBody);

        PassengerRating actual = passengerRatingRepository.findById(expectedId)
                .orElseThrow();

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("badScoreRequests")
    void addScoreToPassenger_shouldReturnBadRequestStatus(ScoreRequest requestBody) {
        ValidationErrorResponse actual = testClient.addScoreToPassengerForBadRequest(requestBody);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void getPassengerRatingsWithComments_shouldReturnPageWithExpectedSize() {
        int passengerId = 1;
        int expectedTotalElements = 2;

        testClient.getPassengerRatingsWithComments(passengerId)
                .body("totalElements", is(expectedTotalElements));
    }

    private static Stream<ScoreRequest> badScoreRequests() {
        return Stream.of(
                new ScoreRequest(null, 2, null), // null userId
                new ScoreRequest(1, -1, null), // score less than 0
                new ScoreRequest(null, -1, null), // null userId and score less than 0
                new ScoreRequest(1, 7, null), // score more than 5
                new ScoreRequest(null, -1, null) // null userId and score more than 5
        );
    }
}
