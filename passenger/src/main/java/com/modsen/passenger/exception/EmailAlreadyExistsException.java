package com.modsen.passenger.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends BaseException {

    public EmailAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT.value(), message);
    }
}
