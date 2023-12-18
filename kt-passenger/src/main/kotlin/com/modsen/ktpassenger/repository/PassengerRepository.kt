package com.modsen.ktpassenger.repository

import com.modsen.ktpassenger.model.Passenger
import org.springframework.data.jpa.repository.JpaRepository

interface PassengerRepository : JpaRepository<Passenger, Int>