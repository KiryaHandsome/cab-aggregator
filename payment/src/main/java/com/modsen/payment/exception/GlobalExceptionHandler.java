package com.modsen.payment.exception;

import com.modsen.payment.dto.ErrorResponse;
import com.modsen.payment.dto.PaymentInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBase(BaseException ex) {
        log.warn("Base exception caught: {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse(ex.getResponseCode(), ex.getMessage());
        return ResponseEntity
                .status(ex.getResponseCode())
                .body(response);
    }

    @ExceptionHandler(LowBalanceException.class)
    public ResponseEntity<PaymentInfo> handleLowBalance(LowBalanceException ex) {
        log.warn("LowBalanceException caught: {}", ex.getMessage());
        PaymentInfo response = new PaymentInfo(ex.getMessage());
        return ResponseEntity
                .status(ex.getResponseCode())
                .body(response);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<PaymentInfo> handleOptimisticLockingFailure(OptimisticLockingFailureException ex) {
        log.warn("OptimisticLockingFailureException caught: {}", ex.getMessage());
        PaymentInfo response = new PaymentInfo(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
        log.warn("Exception caught: {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(response);
    }
}
