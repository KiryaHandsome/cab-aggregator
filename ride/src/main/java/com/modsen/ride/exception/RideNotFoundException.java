package com.modsen.ride.exception;

import org.springframework.http.HttpStatus;

public class RideNotFoundException extends BaseException {

    public RideNotFoundException(String message) {
        super(HttpStatus.NO_CONTENT.value(), message);
    }
}
