package com.modsen.ride.service;

import com.modsen.ride.dto.RideRequest;
import com.modsen.ride.dto.RideResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RideService {

    Page<RideResponse> findByPassengerId(Integer passengerId, Pageable pageable);

    Page<RideResponse> findByDriverId(Integer driverId, Pageable pageable);

    void bookRide(RideRequest rideRequest);
}
