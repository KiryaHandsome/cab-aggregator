package com.modsen.ride.exception;

import com.modsen.ride.dto.DriverStatus;
import org.springframework.http.HttpStatus;

public class DriverNotAvailableException extends BaseException {

    public DriverNotAvailableException(String message, Integer id, DriverStatus driverStatus) {
        super(HttpStatus.BAD_REQUEST.value(), message, id, driverStatus);
    }
}
