package com.modsen.rating.exception;

import org.springframework.http.HttpStatus;

public class RatingNotFoundException extends BaseException {

    public RatingNotFoundException(String messageCode, Integer id) {
        super(HttpStatus.NOT_FOUND.value(), messageCode, id);
    }
}
