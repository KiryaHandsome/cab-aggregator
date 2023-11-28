package com.modsen.ride.exception;

import org.springframework.http.HttpStatus;

public class PaymentFailedException extends BaseException {

    public PaymentFailedException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
