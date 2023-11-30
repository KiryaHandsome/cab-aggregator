package com.modsen.ride.exception;

import org.springframework.http.HttpStatus;

public class RideNotFoundException extends BaseException {

    public RideNotFoundException(String message, String id) {
        super(HttpStatus.NOT_FOUND.value(), message, id);
    }
}
