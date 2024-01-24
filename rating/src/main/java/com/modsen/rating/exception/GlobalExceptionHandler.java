package com.modsen.rating.exception;

import com.modsen.rating.dto.ErrorResponse;
import com.modsen.rating.dto.ValidationErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBase(BaseException ex) {
        String errorMessage = messageSource.getMessage(ex.getMessage(), ex.getParams(), LocaleContextHolder.getLocale());
        ErrorResponse response = new ErrorResponse(ex.getResponseCode(), errorMessage);
        log.info("BaseException caught. Response: {}", response);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> errorsMessages = bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .map(m -> messageSource.getMessage(m, null, LocaleContextHolder.getLocale()))
                .toList();
        ValidationErrorResponse response = new ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), errorsMessages);
        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
        log.warn("{} caught with message {}", ex.getClass().getName(), ex.getMessage());
        ErrorResponse response = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(response);
    }
}
