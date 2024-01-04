package com.modsen.passenger.e2e;

import com.modsen.passenger.PassengerApplication;
import com.modsen.passenger.integration.PostgresContainer;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@RunWith(Cucumber.class)
@CucumberContextConfiguration
@CucumberOptions(features = "classpath:features")
@SpringBootTest(
        classes = {PassengerApplication.class, RunCucumberTest.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class RunCucumberTest extends PostgresContainer {

}
