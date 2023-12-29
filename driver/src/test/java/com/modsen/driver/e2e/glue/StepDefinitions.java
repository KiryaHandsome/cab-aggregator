package com.modsen.driver.e2e.glue;

import com.modsen.driver.dto.request.DriverCreate;
import com.modsen.driver.dto.request.DriverUpdate;
import com.modsen.driver.dto.response.DriverResponse;
import com.modsen.driver.mapper.DriverMapper;
import com.modsen.driver.repository.DriverRepository;
import com.modsen.driver.util.DbTestUtil;
import com.modsen.driver.util.HostUtil;
import com.modsen.driver.util.TestConstants;
import com.modsen.driver.util.TestEntities;
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
    private DriverRepository driverRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DriverMapper driverMapper;

    @LocalServerPort
    private Integer port;

    private String url;
    private ResponseEntity<?> response;
    private DriverCreate requestBody;

    @PostConstruct
    public void initUrl() {
        url = HostUtil.getHost() + port + "/api/v1/drivers";
    }

    @Before
    public void setUpDb() {
        DbTestUtil.executeScript(TestConstants.DELETE_SCRIPT_PATH, jdbcTemplate);
        DbTestUtil.executeScript(TestConstants.CREATE_SCRIPT_PATH, jdbcTemplate);
    }

    @Given("driver with id {int} exists")
    public void driverWithIdExists(int driverId) {
        assertThat(driverRepository.findById(driverId)).isPresent();
    }

    @When("send request to get driver with id {int}")
    public void sendRequestToGetDriverWithId(int driverId) {
        response = restTemplate.getForEntity(url + "/{id}", DriverResponse.class, driverId);
    }

    @Then("response status code should be {int}")
    public void responseStatusCodeShouldBe(int statusCode) {
        assertThat(response.getStatusCode().value()).isEqualTo(statusCode);
    }

    @And("should return john doe")
    public void shouldReturnJohnDoe() {
        var expected = driverMapper.toResponse(TestEntities.johnDoe());
        assertThat(response.getBody()).isEqualTo(expected);
    }

    @When("send delete request with id {int}")
    public void sendDeleteRequest(int driverId) {
        response = restTemplate.exchange(url + "/{id}", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class, driverId);
    }

    @And("driver with id {int} should not exist")
    public void driverWithIdDoesntExist(int driverId) {
        assertThat(driverRepository.findById(driverId)).isNotPresent();
    }

    @When("send patch request for driver with id {int} with email {string}")
    public void sendPatchRequestWithEmail(int driverId, String email) {
        var request = new DriverUpdate(null, null, email, null, null);
        response = restTemplate.exchange(url + "/{id}", HttpMethod.PATCH,
                new HttpEntity<>(request), DriverResponse.class, driverId);
    }

    @Given("request body where name {string} surname {string} email {string} phone {string}")
    public void requestBodyForCreate(String name, String surname, String email, String phoneNumber) {
        requestBody = new DriverCreate(name, surname, email, phoneNumber);
    }

    @When("send post request")
    public void sendPostRequest() {
        response = restTemplate.postForEntity(url, requestBody, DriverResponse.class);
    }

    @When("send request to get all drivers")
    public void sendRequestToGetAllDrivers() {
        response = restTemplate.getForEntity(url, Object.class);
    }
}
