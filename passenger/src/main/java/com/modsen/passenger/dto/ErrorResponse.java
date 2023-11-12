package com.modsen.passenger.dto;

public record ErrorResponse(
        Integer statusCode,
        String errorMessage
) {

}
