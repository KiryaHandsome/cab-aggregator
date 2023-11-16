package com.modsen.driver.exception;

import org.springframework.http.HttpStatus;

public class NoAvailableDriversException extends BaseException {

    public NoAvailableDriversException(String message) {
        super(HttpStatus.NOT_FOUND.value(), message);
    }
}
