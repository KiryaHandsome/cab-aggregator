package com.modsen.driver.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends BaseException {

    public EmailAlreadyExistsException(String message, String email) {
        super(HttpStatus.CONFLICT.value(), message, email);
    }
}
