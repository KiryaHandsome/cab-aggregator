package com.modsen.ride.exception;

import org.springframework.http.HttpStatus;

public class WaitingRideNotFoundException extends BaseException {

    public WaitingRideNotFoundException(String message, String id) {
        super(HttpStatus.NOT_FOUND.value(), message, id);
    }
}
