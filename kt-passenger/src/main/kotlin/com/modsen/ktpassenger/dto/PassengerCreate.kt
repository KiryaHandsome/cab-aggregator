package com.modsen.ktpassenger.dto

import com.modsen.ktpassenger.model.Passenger

data class PassengerCreate(
    val name: String,
    val surname: String,
    val email: String,
    val phoneNumber: String,
)

fun PassengerCreate.toEntity() = Passenger(
    name = name,
    surname = surname,
    email = email,
    phoneNumber = phoneNumber,
)
