package com.modsen.ktpassenger.exception

import com.modsen.ktpassenger.dto.ErrorResponse
import com.modsen.ktpassenger.dto.ValidationErrorResponse
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler(
    private val messageSource: MessageSource
) {

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntime(ex: RuntimeException): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            messageSource.getMessage(ex.message ?: "Internal server error", null, LocaleContextHolder.getLocale())
        )
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .body(response)
    }

    @ExceptionHandler(BaseException::class)
    fun handleBase(ex: BaseException): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            ex.statusCode,
            messageSource.getMessage(ex.errorMessage, ex.params, LocaleContextHolder.getLocale())
        )
        return ResponseEntity
            .status(ex.statusCode)
            .body(response)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ValidationErrorResponse> {
        val bindingResult = ex.bindingResult
        val errorsMessages = bindingResult.allErrors
            .stream()
            .map<String?> { obj: ObjectError -> obj.defaultMessage }
            .map { m: String ->
                messageSource.getMessage(
                    m,
                    null,
                    LocaleContextHolder.getLocale()
                )
            }
            .toList()
        val response = ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), errorsMessages)
        return ResponseEntity
            .badRequest()
            .body(response)
    }
}