package com.modsen.driver.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final Integer responseCode;
    private final Object[] params;

    public BaseException(Integer responseCode, String message, Object... params) {
        super(message);
        this.responseCode = responseCode;
        this.params = params;
    }
}
