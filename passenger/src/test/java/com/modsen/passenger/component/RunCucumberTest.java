package com.modsen.passenger.component;

import com.modsen.passenger.PassengerApplication;
import com.modsen.passenger.integration.BaseIntegrationTest;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Cucumber.class)
@CucumberContextConfiguration
@CucumberOptions(features = "classpath:features")
@SpringBootTest(
        classes = {PassengerApplication.class, RunCucumberTest.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class RunCucumberTest extends BaseIntegrationTest {

}
