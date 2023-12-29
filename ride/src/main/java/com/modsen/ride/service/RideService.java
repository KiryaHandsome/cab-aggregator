package com.modsen.ride.service;

import com.modsen.ride.dto.RideDto;
import com.modsen.ride.dto.request.PaymentEvent;
import com.modsen.ride.dto.request.RideRequest;
import com.modsen.ride.dto.response.SharedRideResponse;
import com.modsen.ride.dto.response.WaitingRideResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RideService {

    WaitingRideResponse bookRide(RideRequest rideRequest);

    Page<RideDto> findByPassengerId(Integer passengerId, Pageable pageable);

    Page<RideDto> findByDriverId(Integer driverId, Pageable pageable);

    Page<WaitingRideResponse> findWaitingRides(Pageable pageable);

    RideDto startRide(String waitingRideId, Integer driverId);

    RideDto endRide(String rideId);

    void handlePaymentResult(PaymentEvent paymentEvent);

    SharedRideResponse haveSharedRide(Integer driverId, Integer passengerId);
}
