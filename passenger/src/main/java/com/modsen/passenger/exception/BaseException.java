package com.modsen.passenger.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final Object[] params;
    private final Integer responseCode;

    public BaseException(Integer responseCode, String message, Object... params) {
        super(message);
        this.responseCode = responseCode;
        this.params = params;
    }
}
