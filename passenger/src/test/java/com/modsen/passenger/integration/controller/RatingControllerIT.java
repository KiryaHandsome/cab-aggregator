package com.modsen.passenger.integration.controller;

import com.modsen.passenger.dto.request.ScoreRequest;
import com.modsen.passenger.dto.response.RatingResponse;
import com.modsen.passenger.dto.response.ValidationErrorResponse;
import com.modsen.passenger.integration.BaseIntegrationTest;
import com.modsen.passenger.integration.testclient.RatingTestClient;
import com.modsen.passenger.util.TestEntities;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.text.DecimalFormat;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(
        scripts = {"classpath:sql/delete-all.sql", "classpath:sql/create-data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class RatingControllerIT extends BaseIntegrationTest {

    @LocalServerPort
    private Integer port;

    private RatingTestClient ratingTestClient;

    @PostConstruct
    void setUp() {
        ratingTestClient = RatingTestClient.withPort(port);
    }

    @Test
    void getRating_shouldReturnExpectedRating() {
        Integer passengerId = 1;
        RatingResponse expected = new RatingResponse(TestEntities.JOHN_AVERAGE_RATING, TestEntities.JOHN_TOTAL_RATINGS);

        RatingResponse actual = ratingTestClient.getRating(passengerId);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @SneakyThrows
    void addScore_shouldReturnUpdatedRating() {
        int passengerId = 1;
        float initialAverage = 4.f;
        int initialTotal = 5;
        int newScore = 5;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedAverage = decimalFormat.format((initialAverage * initialTotal + newScore) / (initialTotal + 1));
        RatingResponse expected = new RatingResponse(
                Float.parseFloat(formattedAverage),
                initialTotal + 1
        );

        RatingResponse actual = ratingTestClient.addScore(passengerId, new ScoreRequest(newScore));

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 6, 100, 100000})
    @SneakyThrows
    void addScore_shouldReturnBadRequest(int newScore) {
        int passengerId = 1;

        ValidationErrorResponse actual = ratingTestClient.addScoreForValidationError(
                passengerId, new ScoreRequest(newScore)
        );

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}