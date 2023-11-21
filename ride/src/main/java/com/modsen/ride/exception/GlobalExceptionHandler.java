package com.modsen.ride.exception;

import com.modsen.ride.dto.ErrorResponse;
import com.modsen.ride.dto.ValidationErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        log.warn("Caught runtime exception: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()
        );
        return ResponseEntity
                .internalServerError()
                .body(errorResponse);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> handleBase(BaseException ex) {
        log.warn("Caught base exception: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ex.getResponseCode(), ex.getMessage());
        return ResponseEntity
                .status(ex.getResponseCode())
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("Caught validation exception: {}", ex.getMessage());
        BindingResult bindingResult = ex.getBindingResult();
        List<String> errorsMessages = bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        ValidationErrorResponse response = new ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), errorsMessages);
        return ResponseEntity
                .badRequest()
                .body(response);
    }
}
