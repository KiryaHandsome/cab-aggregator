package com.modsen.rating.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final Integer responseCode;
    private final Object[] params;

    public BaseException(Integer responseCode, String messageCode, Object... params) {
        super(messageCode);
        this.responseCode = responseCode;
        this.params = params;
    }
}
