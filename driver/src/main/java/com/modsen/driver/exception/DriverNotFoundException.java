package com.modsen.driver.exception;

import org.springframework.http.HttpStatus;

public class DriverNotFoundException extends BaseException {

    public DriverNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND.value(), message);
    }
}
