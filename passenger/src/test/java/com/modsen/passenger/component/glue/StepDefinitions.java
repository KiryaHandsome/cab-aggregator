package com.modsen.passenger.component.glue;

import com.modsen.passenger.component.RunCucumberTest;
import com.modsen.passenger.dto.response.PassengerResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;


public class StepDefinitions extends RunCucumberTest {

    @Autowired
    private TestRestTemplate testClient;
    private Integer passengerId;
    private ResponseEntity<PassengerResponse> response;

    @Given("Client provides id {int}")
    public void clientProvidesId(Integer passengerId) {
        this.passengerId = passengerId;
    }

    @When("Client makes request to {string}")
    public void clientMakesRequestToApiVPassengers(String endpoint) {
        response = testClient
                .getForEntity(endpoint + passengerId, PassengerResponse.class);
    }

    @Then("Should return OK status and expected passenger")
    public void shouldReturnOkStatusAndExpectedPassenger() {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
