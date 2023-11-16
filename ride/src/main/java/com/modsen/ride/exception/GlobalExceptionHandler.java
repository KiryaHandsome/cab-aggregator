package com.modsen.ride.exception;

import com.modsen.ride.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(500, ex.getMessage() + Arrays.toString(ex.getStackTrace()));
        return ResponseEntity
                .internalServerError()
                .body(errorResponse);
    }
}
