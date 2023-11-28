package com.modsen.payment.integration.controller;

import com.modsen.payment.dto.PaymentInfo;
import com.modsen.payment.dto.RideInfo;
import com.modsen.payment.integration.BaseIntegrationTest;
import com.modsen.payment.model.Balance;
import com.modsen.payment.repository.BalanceRepository;
import com.modsen.payment.util.HostUtil;
import com.modsen.payment.util.TestData;
import io.restassured.http.ContentType;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

@Sql(
        scripts = {"classpath:sql/delete-data.sql", "classpath:sql/create-data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class PaymentControllerIT extends BaseIntegrationTest {

    @LocalServerPort
    private Integer port;

    private String url;

    @Autowired
    private BalanceRepository balanceRepository;

    @BeforeEach
    void setUp() {
        url = HostUtil.getHost() + port;
    }

    //@formatter:off
    @Test
    public void payForRide_withNonExistingPassengerId_shouldReturnNotFound() {
        RideInfo rideInfo = TestData.defaultRideInfo();

        given()
                .body(rideInfo)
                .contentType(ContentType.JSON)
        .when()
                .post(url + "/api/v1/payment/ride")
        .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body(notNullValue(PaymentInfo.class))
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void payForRide_withCostMoreThanBalance_shouldReturnBadRequest() {
        Float cost = 100.f;
        Integer passengerId = 1;
        RideInfo rideInfo = TestData.defaultRideInfo();
        rideInfo.setCost(cost);
        rideInfo.setPassengerId(passengerId);

        given()
                .body(rideInfo)
                .contentType(ContentType.JSON)
        .when()
                .post(url + "/api/v1/payment/ride")
        .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body(notNullValue(PaymentInfo.class))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void payForRide_shouldUpdatePassengerBalance() {
        Integer passengerId = 1;
        Float passengerBalance = 90.88f;
        RideInfo rideInfo = TestData.defaultRideInfo();
        rideInfo.setPassengerId(passengerId);
        Float expectedAmount = passengerBalance - rideInfo.getCost();

        given()
                .body(rideInfo)
                .contentType(ContentType.JSON)
        .when()
                .post(url + "/api/v1/payment/ride")
        .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .body(notNullValue(PaymentInfo.class))
                .statusCode(HttpStatus.OK.value());

        Balance actual = balanceRepository.findByPassengerId(passengerId)
                .orElseThrow();

        assertThat(actual.getAmount())
                .isCloseTo(expectedAmount, Offset.offset(0.001f));
    }
}
