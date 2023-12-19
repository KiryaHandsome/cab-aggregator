package com.modsen.ktpassenger.util

import com.modsen.ktpassenger.dto.PassengerCreate
import com.modsen.ktpassenger.dto.PassengerResponse
import com.modsen.ktpassenger.dto.PassengerUpdate
import com.modsen.ktpassenger.model.Passenger


object TestData {
    const val PASSENGER_ID = 1
    const val NAME = "Some name"
    const val SURNAME = "Surname"
    const val EMAIL = "email@test.com"
    const val PHONE_NUMBER = "+375445556677"
    const val NEW_NAME = "Some new name"
    const val NEW_SURNAME = "NEW Surname"
    const val NEW_EMAIL = "new_email@test.com"
    const val NEW_PHONE_NUMBER = "+375294447788"


    fun defaultPassenger(): Passenger {
        return Passenger(
            PASSENGER_ID,
            NAME,
            SURNAME,
            EMAIL,
            PHONE_NUMBER
        )
    }

    fun defaultPassengerCreate(): PassengerCreate {
        return PassengerCreate(
            NAME,
            SURNAME,
            EMAIL,
            PHONE_NUMBER
        )
    }

    fun newPassengerCreate(): PassengerCreate {
        return PassengerCreate(
            NEW_NAME,
            NEW_SURNAME,
            NEW_EMAIL,
            NEW_PHONE_NUMBER
        )
    }

    fun defaultPassengerUpdate(): PassengerUpdate {
        return PassengerUpdate(
            NAME,
            SURNAME,
            EMAIL,
            PHONE_NUMBER
        )
    }

    fun defaultPassengerResponse(): PassengerResponse {
        return PassengerResponse(
            PASSENGER_ID,
            NAME,
            SURNAME,
            EMAIL,
            PHONE_NUMBER
        )
    }
}

