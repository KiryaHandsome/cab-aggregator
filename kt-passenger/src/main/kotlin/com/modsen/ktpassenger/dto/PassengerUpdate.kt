package com.modsen.ktpassenger.dto

import com.modsen.ktpassenger.model.Passenger

data class PassengerUpdate(
    val name: String?,
    val surname: String?,
    val email: String?,
    val phoneNumber: String?,
)

fun Passenger.mapIfNotNull(passenger: PassengerUpdate) {
    name = passenger.name ?: name
    surname = passenger.surname ?: surname
    email = passenger.email ?: email
    phoneNumber = passenger.phoneNumber ?: phoneNumber
}