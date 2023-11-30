package com.modsen.passenger.exception;

import org.springframework.http.HttpStatus;

public class PassengerNotFoundException extends BaseException {

    public PassengerNotFoundException(String message, Integer id) {
        super(HttpStatus.NOT_FOUND.value(), message, id);
    }
}
