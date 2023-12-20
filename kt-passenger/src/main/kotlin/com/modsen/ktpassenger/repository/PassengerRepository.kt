package com.modsen.ktpassenger.repository

import com.modsen.ktpassenger.model.Passenger
import java.util.Optional
import org.springframework.data.jpa.repository.JpaRepository

interface PassengerRepository : JpaRepository<Passenger, Int> {
    fun findByEmail(email: String): Optional<Passenger>

    fun findByPhoneNumber(phoneNumber: String): Optional<Passenger>
}