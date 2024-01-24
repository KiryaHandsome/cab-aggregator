package com.modsen.rating.integration.controller;

import com.modsen.rating.dto.AvgRatingResponse;
import com.modsen.rating.dto.ScoreRequest;
import com.modsen.rating.dto.ErrorResponse;
import com.modsen.rating.dto.ValidationErrorResponse;
import com.modsen.rating.integration.PostgresContainer;
import com.modsen.rating.integration.testclient.DriverRatingTestClient;
import com.modsen.rating.model.DriverRating;
import com.modsen.rating.repository.DriverRatingRepository;
import com.modsen.rating.util.TestConstants;
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
        scripts = {"classpath:" + TestConstants.DELETE_SCRIPT_PATH, "classpath:" + TestConstants.CREATE_SCRIPT_PATH},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DriverControllerIT extends PostgresContainer {

    @LocalServerPort
    private Integer port;

    @Autowired
    private DriverRatingRepository driverRatingRepository;
    private DriverRatingTestClient testClient;

    @PostConstruct
    void setUp() {
        testClient = DriverRatingTestClient.withPort(port);
    }

    @Test
    void getAverageDriverRating_shouldReturnExpectedAvgRating() {
        int driverId = 1;
        float expectedAvgRating = 3f;

        AvgRatingResponse actual = testClient.getAverageDriverRatingForOk(driverId);

        assertThat(actual.getAverageRating()).isCloseTo(expectedAvgRating, Offset.offset(0.01f));
    }

    @Test
    void getAverageDriverRating_shouldReturnNotFoundStatus() {
        int driverId = 100;

        ErrorResponse actual = testClient.getAverageDriverRatingForNotFound(driverId);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void addScoreToDriver_shouldReturnCreatedStatusAndSaveRatingToDb() {
        int expectedId = 4;
        DriverRating expected = new DriverRating(expectedId, TestData.DRIVER_ID, TestData.SCORE, TestData.COMMENT);
        ScoreRequest requestBody = new ScoreRequest(TestData.SCORE, TestData.COMMENT);

        testClient.addScoreToDriverForCreated(TestData.DRIVER_ID, requestBody);

        DriverRating actual = driverRatingRepository.findById(expectedId)
                .orElseThrow();

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("badScoreRequests")
    void addScoreToDriver_shouldReturnBadRequestStatus(ScoreRequest requestBody) {
        ValidationErrorResponse actual = testClient.addScoreToDriverForBadRequest(TestData.DRIVER_ID, requestBody);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void getDriverRatingsWithComments_shouldReturnPageWithExpectedSize() {
        int driverId = 1;
        int expectedTotalElements = 2;

        testClient.getDriverRatingsWithComments(driverId)
                .body("totalElements", is(expectedTotalElements));
    }

    private static Stream<ScoreRequest> badScoreRequests() {
        return Stream.of(
                new ScoreRequest(-1, null), // score less than 0
                new ScoreRequest(7, null) // score more than 5
        );
    }
}
