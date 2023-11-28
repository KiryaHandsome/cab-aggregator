package com.modsen.driver.integration.controller;

import com.modsen.driver.dto.request.ScoreRequest;
import com.modsen.driver.integration.BaseIntegrationTest;
import com.modsen.driver.model.Rating;
import com.modsen.driver.repository.RatingRepository;
import com.modsen.driver.util.HostUtil;
import com.modsen.driver.util.TestEntities;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

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
class RatingControllerIntegrationTest extends BaseIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @LocalServerPort
    private Integer port;

    @Autowired
    private RatingRepository ratingRepository;

    //@formatter:off
    @Test
    void getRating_shouldReturnExpectedRating() {
        Integer driverId = 1;

        when()
                .get(HostUtil.getHost() + port + "/api/v1/ratings/{driverId}", driverId)
        .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body("averageRating", is(TestEntities.JOHN_AVERAGE_RATING))
                .body("totalRatings", is(TestEntities.JOHN_TOTAL_RATINGS))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @SneakyThrows
    void addScore_shouldReturnUpdatedRating() {
        Integer driverId = 1;
        int newScore = 5;
        Rating rating = ratingRepository.findByDriverId(driverId)
                .orElseThrow();
        Float initialAverage = rating.getAverageRating();
        Integer initialTotal = rating.getTotalRatings();
        String requestBody = objectMapper.writeValueAsString(new ScoreRequest(newScore));

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post(HostUtil.getHost() + port + "/api/v1/ratings/{driverId}", driverId)
        .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body("averageRating", notNullValue())
                .body("totalRatings", notNullValue())
                .statusCode(HttpStatus.OK.value());

        rating = ratingRepository.findByDriverId(driverId)
                .orElseThrow();

        assertThat(rating.getTotalRatings()).isEqualTo(initialTotal + 1);
        assertThat(rating.getAverageRating())
                .isCloseTo((initialAverage * initialTotal + newScore) / (initialTotal + 1), Offset.offset(0.01f));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 6, 100, 100000})
    @SneakyThrows
    void addScore_shouldReturnBadRequest(int newScore) {
        Integer driverId = 1;
        String requestBody = objectMapper.writeValueAsString(new ScoreRequest(newScore));

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post(HostUtil.getHost() + port + "/api/v1/ratings/{driverId}", driverId)
        .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body("statusCode", is(HttpStatus.BAD_REQUEST.value()))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}