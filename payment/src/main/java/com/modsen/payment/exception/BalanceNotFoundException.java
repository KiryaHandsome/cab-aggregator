package com.modsen.payment.exception;

import org.springframework.http.HttpStatus;

public class BalanceNotFoundException extends BaseException {

    public BalanceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND.value(), message);
    }
}
