package com.modsen.rating.e2e.glue;

import com.modsen.rating.dto.AvgRatingResponse;
import com.modsen.rating.dto.ScoreRequest;
import com.modsen.rating.integration.util.HostUtil;
import com.modsen.rating.repository.DriverRatingRepository;
import com.modsen.rating.repository.PassengerRatingRepository;
import com.modsen.rating.util.DbTestUtil;
import com.modsen.rating.util.TestConstants;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.annotation.PostConstruct;
import org.assertj.core.data.Offset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class StepDefinitions {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DriverRatingRepository driverRatingRepository;

    @Autowired
    private PassengerRatingRepository passengerRatingRepository;

    @LocalServerPort
    private Integer port;
    private String driverUrl;
    private String passengerUrl;
    private ResponseEntity<?> response;
    private ScoreRequest request;

    @PostConstruct
    public void initUrl() {
        driverUrl = HostUtil.getHost() + port + "/api/v1/ratings/drivers";
        passengerUrl = HostUtil.getHost() + port + "/api/v1/ratings/passengers";
    }

    @Before
    public void setUpDb() {
        DbTestUtil.executeScript(TestConstants.DELETE_SCRIPT_PATH, jdbcTemplate);
        DbTestUtil.executeScript(TestConstants.CREATE_SCRIPT_PATH, jdbcTemplate);
    }

    @Given("score request with score {int} and comment {string}")
    public void scoreRequestWithScoreAndComment(int score, String comment) {
        request = new ScoreRequest(score, comment);
    }

    @When("add score to driver with id {int}")
    public void addScoreToDriverWithId(int driverId) {
        response = restTemplate.postForEntity(driverUrl + "/{id}", request, Void.class, driverId);
    }

    @Then("there should be new driver rating with id {int}")
    public void thereShouldBeNewDriverRatingWithId(int id) {
        assertThat(driverRatingRepository.findById(id)).isPresent();
    }

    @Then("response status should be {int}")
    public void responseStatusShouldBe(int statusCode) {
        assertThat(response.getStatusCode().value()).isEqualTo(statusCode);
    }


    @When("get average rating of driver with id {int}")
    public void getAverageRatingOfDriverWithId(int driverId) {
        response = restTemplate.getForEntity(driverUrl + "/{id}/avg", AvgRatingResponse.class, driverId);
    }

    @Then("rating should be close to {float}")
    public void ratingShouldBeCloseTo(float expected) {
        AvgRatingResponse rating = (AvgRatingResponse) response.getBody();
        assertThat(rating.getAverageRating()).isCloseTo(expected, Offset.offset(0.01f));
    }

    @When("add score to passenger with id {int}")
    public void addScoreToPassengerWithId(int passengerId) {
        response = restTemplate.postForEntity(passengerUrl + "/{id}", request, Void.class, passengerId);
    }

    @Then("there should be new passenger rating with id {int}")
    public void thereShouldBeNewPassengerRatingWithId(int id) {
        assertThat(passengerRatingRepository.findById(id)).isPresent();
    }

    @When("get average rating of passenger with id {int}")
    public void getAverageRatingOfPassengerWithId(int passengerId) {
        response = restTemplate.getForEntity(passengerUrl + "/{id}/avg", AvgRatingResponse.class, passengerId);
    }
}
