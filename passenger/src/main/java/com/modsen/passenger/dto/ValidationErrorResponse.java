package com.modsen.passenger.dto;

import java.util.List;

public record ValidationErrorResponse(
        Integer statusCode,
        List<String> errors
) {

}
