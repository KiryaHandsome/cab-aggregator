package com.modsen.ktpassenger.dto

import com.modsen.ktpassenger.model.Passenger
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class PassengerUpdate(
    @field:Size(min = 2, max = 255, message = "passenger.name.size")
    val name: String?,

    @field:Size(min = 2, max = 255, message = "passenger.surname.size")
    val surname: String?,

    @field:Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,}$", message = "passenger.email.email")
    val email: String?,

    @field:Pattern(regexp = "^\\+\\d{12}$", message = "passenger.phone.pattern")
    val phoneNumber: String?,
)

fun Passenger.mapIfNotNull(passenger: PassengerUpdate) {
    name = passenger.name ?: name
    surname = passenger.surname ?: surname
    email = passenger.email ?: email
    phoneNumber = passenger.phoneNumber ?: phoneNumber
}