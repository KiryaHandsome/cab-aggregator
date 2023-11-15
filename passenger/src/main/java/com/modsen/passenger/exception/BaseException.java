package com.modsen.passenger.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final Integer responseCode;

    public BaseException(Integer responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
    }
}
