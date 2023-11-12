package com.modsen.passenger.exception;

import com.modsen.passenger.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBase(BaseException ex) {
        ErrorResponse response = new ErrorResponse(ex.getResponseCode(), ex.getMessage());
        return ResponseEntity
                .status(response.statusCode())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder errorMessage = new StringBuilder("Validation error(s): ");
        bindingResult.getAllErrors()
                .forEach(error -> errorMessage
                        .append(error.getDefaultMessage())
                        .append(", ")
                );
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage.toString());
        return ResponseEntity
                .badRequest()
                .body(response);
    }

}
