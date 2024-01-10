package com.modsen.ride.component.glue;


import com.modsen.ride.dto.DriverStatus;
import com.modsen.ride.dto.request.StatusUpdate;
import com.modsen.ride.dto.response.DriverResponse;
import com.modsen.ride.exception.RideAlreadyEndedException;
import com.modsen.ride.exception.WaitingRideNotFoundException;
import com.modsen.ride.model.Ride;
import com.modsen.ride.model.WaitingRide;
import com.modsen.ride.repository.RideRepository;
import com.modsen.ride.repository.WaitingRideRepository;
import com.modsen.ride.service.RideService;
import com.modsen.ride.service.client.DriverClient;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

    @Autowired
    private RideRepository rideRepository;

    // result variables
    private Exception exception;

    @Before
    public void setUp() {
        exception = null;
    }

    @Given("Driver with id {int} has status {string}")
    public void setDriverStatus(Integer id, String driverStatus) {
        doReturn(new DriverResponse(id, null, null, null, null, DriverStatus.valueOf(driverStatus)))
                .when(driverClient)
                .getDriverById(id);
    }

    @Given("There is waiting ride with id {string}")
    public void waitingRideWithId(String waitingRideId) {
        doReturn(Optional.of(new WaitingRide(waitingRideId, null, null, null)))
                .when(waitingRideRepository)
                .findById(waitingRideId);
    }

    @When("Start ride with waiting ride id {string} and driver id {int}")
    public void startRide(String waitingRideId, Integer driverId) {
        try {
            rideService.startRide(waitingRideId, driverId);
        } catch (WaitingRideNotFoundException e) {
            exception = e;
        }
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

    @Given("There is no waiting ride with id {string}")
    public void noWaitingRideWithId(String waitingRideId) {
        doReturn(Optional.empty())
                .when(waitingRideRepository)
                .findById(waitingRideId);
    }

    @Then("Should throw waiting ride not found exception")
    public void checkWaitingRideNotFoundException() {
        assertThat(exception).isNotNull();
    }

    @When("End ride with ride id {string}")
    public void endRideWithRideId(String rideId) {
        rideService.endRide(rideId);
    }

    @And("Should update ride finish time")
    public void shouldUpdateRideFinishTime() {
        rideRepository.save(any());
    }

    @Given("Ride with id {string} already ended")
    public void rideWithIdAlreadyEnded(String rideId) {
        doReturn(Optional.of(new Ride(null, null, null, null, null, null, null, LocalDateTime.now(), LocalDateTime.now())))
                .when(rideRepository)
                .findById(rideId);
    }

    @When("End ride with waiting ride id {string}")
    public void endRideWithWaitingRideId(String rideId) {
        try {
            rideService.endRide(rideId);
        } catch (RideAlreadyEndedException e) {
            exception = e;
        }
    }

    @Then("Should throw ride already ended exception")
    public void shouldThrowRideAlreadyEndedException() {
        assertThat(exception).isNotNull();
    }

    @Given("There is ride with id {string} and driver id {int}")
    public void thereIsRideWithIdAndDriverId(String rideId, int driverId) {
        doReturn(Optional.of(new Ride(null, driverId, null, null, null, null, null, null, null)))
                .when(rideRepository)
                .findById(rideId);
    }
}
