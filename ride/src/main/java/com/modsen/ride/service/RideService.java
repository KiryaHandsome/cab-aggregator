package com.modsen.ride.service;

import com.modsen.ride.dto.RideRequest;
import com.modsen.ride.dto.RideResponse;
import com.modsen.ride.dto.WaitingRideResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RideService {

    void bookRide(RideRequest rideRequest);

    Page<RideResponse> findByPassengerId(Integer passengerId, Pageable pageable);

    Page<RideResponse> findByDriverId(Integer driverId, Pageable pageable);

    Page<WaitingRideResponse> findWaitingRides(Pageable pageable);

}
