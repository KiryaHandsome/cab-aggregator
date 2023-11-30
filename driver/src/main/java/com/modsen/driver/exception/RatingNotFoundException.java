package com.modsen.driver.exception;

import org.springframework.http.HttpStatus;

public class RatingNotFoundException extends BaseException {

    public RatingNotFoundException(String message, Integer id) {
        super(HttpStatus.NOT_FOUND.value(), message, id);
    }
}
