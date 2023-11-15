package com.modsen.ride.controller.openapi;

import com.modsen.ride.dto.RideRequest;
import com.modsen.ride.dto.RideResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface RideControllerOpenApi {

    ResponseEntity<?> bookRide(RideRequest rideRequest);

    ResponseEntity<Page<RideResponse>> getPassengerRides(Integer passengerId, Pageable pageable);

    ResponseEntity<Page<RideResponse>> getDriverRides(Integer driverId, Pageable pageable);

}
