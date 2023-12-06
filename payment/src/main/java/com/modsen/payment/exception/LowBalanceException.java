package com.modsen.payment.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LowBalanceException extends BaseException {

    public LowBalanceException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
