package com.modsen.driver.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class DriverNotFoundException extends BaseException {

    public DriverNotFoundException(String message, Integer id) {
        super(HttpStatus.NOT_FOUND.value(), message, id);
    }
}
