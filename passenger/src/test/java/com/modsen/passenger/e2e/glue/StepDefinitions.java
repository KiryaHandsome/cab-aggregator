package com.modsen.passenger.e2e.glue;

import com.modsen.passenger.dto.request.PassengerCreate;
import com.modsen.passenger.dto.request.PassengerUpdate;
import com.modsen.passenger.dto.response.PassengerResponse;
import com.modsen.passenger.mapper.PassengerMapper;
import com.modsen.passenger.repository.PassengerRepository;
import com.modsen.passenger.util.DbTestUtil;
import com.modsen.passenger.util.HostUtil;
import com.modsen.passenger.util.TestConstants;
import com.modsen.passenger.util.TestEntities;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class StepDefinitions {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PassengerMapper passengerMapper;

    @LocalServerPort
    private Integer port;

    private String url;
    private ResponseEntity<?> response;
    private PassengerCreate requestBody;

    @PostConstruct
    public void initUrl() {
        url = HostUtil.getHost() + port + "/api/v1/passengers";
    }

    @Before
    public void setUpDb() {
        DbTestUtil.executeScript(TestConstants.DELETE_SCRIPT_PATH, jdbcTemplate);
        DbTestUtil.executeScript(TestConstants.CREATE_SCRIPT_PATH, jdbcTemplate);
    }

    @Given("passenger with id {int} exists")
    public void passengerWithIdExists(int passengerId) {
        assertThat(passengerRepository.findById(passengerId)).isPresent();
    }

    @When("send request to get passenger with id {int}")
    public void sendRequestToGetPassengerWithId(int passengerId) {
        response = restTemplate.getForEntity(url + "/{id}", PassengerResponse.class, passengerId);
    }

    @Then("response status code should be {int}")
    public void responseStatusCodeShouldBe(int statusCode) {
        assertThat(response.getStatusCode().value()).isEqualTo(statusCode);
    }

    @And("should return john doe")
    public void shouldReturnJohnDoe() {
        var expected = passengerMapper.toResponse(TestEntities.johnDoe());
        assertThat(response.getBody()).isEqualTo(expected);
    }

    @When("send delete request with id {int}")
    public void sendDeleteRequest(int passengerId) {
        response = restTemplate.exchange(url + "/{id}", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class, passengerId);
    }

    @And("passenger with id {int} should not exist")
    public void passengerWithIdDoesntExist(int passengerId) {
        assertThat(passengerRepository.findById(passengerId)).isNotPresent();
    }

    @When("send patch request for passenger with id {int} with email {string}")
    public void sendPatchRequestWithEmail(int passengerId, String email) {
        var request = new PassengerUpdate(null, null, email, null);
        response = restTemplate.exchange(url + "/{id}", HttpMethod.PATCH,
                new HttpEntity<>(request), PassengerResponse.class, passengerId);
    }

    @Given("request body where name {string} surname {string} email {string} phone {string}")
    public void requestBodyForCreate(String name, String surname, String email, String phoneNumber) {
        requestBody = new PassengerCreate(name, surname, email, phoneNumber);
    }

    @When("send post request")
    public void sendPostRequest() {
        response = restTemplate.postForEntity(url, requestBody, PassengerResponse.class);
    }

    @When("send request to get all passengers")
    public void sendRequestToGetAllPassengers() {
        response = restTemplate.getForEntity(url, Object.class);
    }
}
