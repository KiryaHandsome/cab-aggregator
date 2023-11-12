package com.modsen.passenger.exception;

import com.modsen.passenger.dto.ErrorResponse;
import com.modsen.passenger.dto.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

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
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> errorsMessages = bindingResult.getAllErrors()
                .stream()
                .map(e -> e.getDefaultMessage())
                .toList();
        ValidationErrorResponse response = new ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), errorsMessages);
        return ResponseEntity
                .badRequest()
                .body(response);
    }

}
