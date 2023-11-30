package com.modsen.passenger.exception;

import org.springframework.http.HttpStatus;

public class RatingNotFoundException extends BaseException {

    public RatingNotFoundException(String message, Integer passengerId) {
        super(HttpStatus.NOT_FOUND.value(), message, passengerId);
    }
}
