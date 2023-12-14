package com.modsen.ride.component.glue;


import com.modsen.ride.dto.DriverStatus;
import com.modsen.ride.dto.StatusUpdate;
import com.modsen.ride.dto.response.DriverResponse;
import com.modsen.ride.model.WaitingRide;
import com.modsen.ride.repository.WaitingRideRepository;
import com.modsen.ride.service.DriverClient;
import com.modsen.ride.service.RideService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;


public class RideServiceStepDefs {

    @Autowired
    private DriverClient driverClient;

    @Autowired
    private RideService rideService;

    @Autowired
    private WaitingRideRepository waitingRideRepository;

    @Given("Driver with id {int} has status {string}")
    public void setDriverStatus(Integer id, String driverStatus) {
        doReturn(new DriverResponse(id, null, null, null, null, DriverStatus.valueOf(driverStatus)))
                .when(driverClient)
                .getDriverById(id);
    }

    @Given("There is waitingRide with id {string}")
    public void setDriverStatus(String waitingRideId) {
        doReturn(Optional.of(new WaitingRide(waitingRideId, null, null, null)))
                .when(waitingRideRepository)
                .findById(waitingRideId);
    }

    @When("Start ride with waitingRideId {string} and driverId {int}")
    public void startRide(String waitingRideId, Integer driverId) {
        rideService.startRide(waitingRideId, driverId);
    }

    @Then("Should change driver with id {int} status to {string}")
    public void shouldChangeDriverStatus(Integer id, String expectedDriverStatus) {
        var expectedRequest = new StatusUpdate(DriverStatus.valueOf(expectedDriverStatus));
        verify(driverClient).updateDriver(eq(id), eq(expectedRequest));
    }

    @Then("Should remove waiting ride with id {string}")
    public void shouldChangeDriverStatus(String waitingRideId) {
        verify(waitingRideRepository).deleteById(waitingRideId);
    }
}
