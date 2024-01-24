package com.modsen.ktpassenger.dto


data class ValidationErrorResponse(
    val statusCode: Int,
    val errors: List<String>
)

