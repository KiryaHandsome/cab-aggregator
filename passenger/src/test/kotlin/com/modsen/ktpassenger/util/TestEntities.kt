package com.modsen.ktpassenger.util

import com.modsen.ktpassenger.model.Passenger


object TestEntities {
    const val JOHN_ID = 1
    const val JOHN_NAME = "John"
    const val JOHN_SURNAME = "Doe"
    const val JOHN_EMAIL = "john.doe@example.com"
    const val JOHN_PHONE_NUMBER = "+375441112222"

    fun johnDoe(): Passenger {
        return Passenger(
            JOHN_ID,
            JOHN_NAME,
            JOHN_SURNAME,
            JOHN_EMAIL,
            JOHN_PHONE_NUMBER
        )
    }
}
