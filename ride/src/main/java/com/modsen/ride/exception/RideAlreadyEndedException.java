package com.modsen.ride.exception;

import org.springframework.http.HttpStatus;

public class RideAlreadyEndedException extends BaseException {

    public RideAlreadyEndedException(String message, String rideId) {
        super(HttpStatus.CONFLICT.value(), message, rideId);
    }
}
