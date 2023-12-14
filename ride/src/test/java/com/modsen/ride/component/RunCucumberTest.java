package com.modsen.ride.component;


import com.modsen.ride.integration.BaseIntegrationTest;
import com.modsen.ride.repository.WaitingRideRepository;
import com.modsen.ride.service.DriverClient;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@RunWith(Cucumber.class)
@CucumberContextConfiguration
@CucumberOptions(
        features = "classpath:features"
)
@SpringBootTest
public class RunCucumberTest extends BaseIntegrationTest {

    @MockBean
    DriverClient driverClient;

    @MockBean
    WaitingRideRepository waitingRideRepository;
}
