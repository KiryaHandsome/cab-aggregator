package com.modsen.ride.exception;

import com.modsen.ride.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()
        );
        return ResponseEntity
                .internalServerError()
                .body(errorResponse);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> handleBase(BaseException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getResponseCode(), ex.getMessage());
        return ResponseEntity
                .status(ex.getResponseCode())
                .body(errorResponse);
    }
}
