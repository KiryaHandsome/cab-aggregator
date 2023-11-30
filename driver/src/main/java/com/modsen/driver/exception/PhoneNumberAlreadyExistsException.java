package com.modsen.driver.exception;

import org.springframework.http.HttpStatus;

public class PhoneNumberAlreadyExistsException extends BaseException {

    public PhoneNumberAlreadyExistsException(String message, String phoneNumber) {
        super(HttpStatus.CONFLICT.value(), message, phoneNumber);
    }
}
