package com.modsen.driver.dto;

public record ErrorResponse(
        Integer statusCode,
        String errorMessage
) {

}
