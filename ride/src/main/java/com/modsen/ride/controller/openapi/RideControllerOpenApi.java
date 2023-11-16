package com.modsen.ride.controller.openapi;

import com.modsen.ride.dto.RideRequest;
import com.modsen.ride.dto.RideResponse;
import com.modsen.ride.dto.WaitingRideResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface RideControllerOpenApi {

    ResponseEntity<?> bookRide(RideRequest rideRequest);

    ResponseEntity<Page<WaitingRideResponse>> getWaitingRides(Pageable pageable);

    ResponseEntity<Page<RideResponse>> getPassengerRides(Integer passengerId, Pageable pageable);

    ResponseEntity<Page<RideResponse>> getDriverRides(@PathVariable Integer driverId, Pageable pageable);
}
