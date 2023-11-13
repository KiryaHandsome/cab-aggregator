package com.modsen.driver.dto;

import java.util.List;

public record ValidationErrorResponse(
        Integer statusCode,
        List<String> errors
) {

}
