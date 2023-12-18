package com.modsen.ktpassenger.exception

import com.modsen.ktpassenger.dto.ErrorEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntime(ex: RuntimeException): ResponseEntity<ErrorEntity> {
        val response = ErrorEntity(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            ex.message ?: "Internal server error"
        )
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .body(response)
    }

    @ExceptionHandler(BaseException::class)
    fun handleBase(ex: BaseException): ResponseEntity<ErrorEntity> {
        val response = ErrorEntity(ex.statusCode, ex.errorMessage)
        return ResponseEntity
            .status(ex.statusCode)
            .body(response)
    }
}