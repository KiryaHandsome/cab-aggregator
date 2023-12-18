package com.modsen.ktpassenger.dto

import com.modsen.ktpassenger.model.Passenger

data class PassengerResponse(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val phoneNumber: String,
)

fun Passenger.toResponse() = PassengerResponse(
    id = id ?: 0,
    name = name,
    surname = surname,
    email = email,
    phoneNumber = phoneNumber,
)
