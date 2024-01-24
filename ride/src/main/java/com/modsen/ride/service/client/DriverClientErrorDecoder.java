package com.modsen.ride.service.client;

import com.modsen.ride.exception.BaseException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class DriverClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (HttpStatus.NOT_FOUND.value() == response.status()) {
            return new BaseException(HttpStatus.NOT_FOUND.value(), "exception.driver_not_found", response.reason());
        }

        return new RuntimeException("Exception occurred when calling driverClient");
    }
}