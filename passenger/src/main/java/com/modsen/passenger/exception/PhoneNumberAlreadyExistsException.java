package com.modsen.passenger.exception;

import org.springframework.http.HttpStatus;

public class PhoneNumberAlreadyExistsException extends BaseException {

    public PhoneNumberAlreadyExistsException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
