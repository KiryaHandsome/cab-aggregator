package com.modsen.driver.integration.controller;

import com.modsen.driver.dto.request.ScoreRequest;
import com.modsen.driver.dto.response.RatingResponse;
import com.modsen.driver.dto.response.ValidationErrorResponse;
import com.modsen.driver.integration.BaseIntegrationTest;
import com.modsen.driver.integration.testclient.RatingTestClient;
import com.modsen.driver.repository.RatingRepository;
import com.modsen.driver.util.TestEntities;
import io.restassured.http.ContentType;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RatingRepository ratingRepository;
    private RatingTestClient ratingTestClient;

    @PostConstruct
    void setUp() {
        ratingTestClient = RatingTestClient.withPort(port);
    }

    @Test
    void getRating_shouldReturnExpectedRating() {
        Integer driverId = 1;
        RatingResponse expected = new RatingResponse(TestEntities.JOHN_AVERAGE_RATING, TestEntities.JOHN_TOTAL_RATINGS);

        RatingResponse actual = ratingTestClient.getRating(driverId)
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(RatingResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @SneakyThrows
    void addScore_shouldReturnUpdatedRating() {
        int driverId = 1;
        float initialAverage = 4.f;
        int initialTotal = 5;
        int newScore = 5;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedAverage = decimalFormat.format((initialAverage * initialTotal + newScore) / (initialTotal + 1));
        RatingResponse expected = new RatingResponse(
                Float.parseFloat(formattedAverage),
                initialTotal + 1
        );

        RatingResponse actual = ratingTestClient.addScore(driverId, new ScoreRequest(newScore))
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(RatingResponse.class);

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 6, 100, 100000})
    @SneakyThrows
    void addScore_shouldReturnBadRequest(int newScore) {
        int driverId = 1;

        ValidationErrorResponse actual = ratingTestClient.addScore(driverId, new ScoreRequest(newScore))
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ValidationErrorResponse.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}