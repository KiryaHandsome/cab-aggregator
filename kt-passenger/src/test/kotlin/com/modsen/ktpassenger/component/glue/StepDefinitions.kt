package com.modsen.ktpassenger.component.glue

import com.modsen.ktpassenger.dto.PassengerResponse
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


class StepDefinitions(
    private val testClient: TestRestTemplate
) {

    private var passengerId: Int? = null
    private var response: ResponseEntity<PassengerResponse>? = null

    @Given("Client provides id {int}")
    fun clientProvidesId(passengerId: Int?) {
        this.passengerId = passengerId
    }

    @When("Client makes request to {string}")
    fun clientMakesRequestToApiPassengers(endpoint: String) {
        response = testClient
            .getForEntity(endpoint + passengerId, PassengerResponse::class.java)
    }

    @Then("Should return OK status and expected passenger")
    fun shouldReturnOkStatus() {
        Assertions.assertThat(response!!.statusCode).isEqualTo(HttpStatus.OK)
    }
}

