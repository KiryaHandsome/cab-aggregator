package com.modsen.ktpassenger.service

import com.modsen.ktpassenger.dto.PassengerCreate
import com.modsen.ktpassenger.dto.PassengerResponse
import com.modsen.ktpassenger.dto.PassengerUpdate
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PassengerService {

    fun findById(id: Int): PassengerResponse

    fun findAll(pageable: Pageable): Page<PassengerResponse>

    fun update(id: Int, request: PassengerUpdate): PassengerResponse

    fun deleteById(id: Int)

    fun create(request: PassengerCreate): PassengerResponse
}