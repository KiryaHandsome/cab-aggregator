package com.modsen.e2e.dto;

public record ErrorResponse(Integer statusCode,
                            String errorMessage) {

}
