package com.modsen.ktpassenger.service.impl

import com.modsen.ktpassenger.dto.PassengerCreate
import com.modsen.ktpassenger.dto.PassengerResponse
import com.modsen.ktpassenger.dto.PassengerUpdate
import com.modsen.ktpassenger.dto.mapIfNotNull
import com.modsen.ktpassenger.dto.toEntity
import com.modsen.ktpassenger.dto.toResponse
import com.modsen.ktpassenger.repository.PassengerRepository
import com.modsen.ktpassenger.service.PassengerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PassengerServiceImpl(
    private val passengerRepository: PassengerRepository
) : PassengerService {

    @Transactional(readOnly = true)
    override fun findById(id: Int): PassengerResponse {
        return passengerRepository.findById(id)
            .map { p -> p.toResponse() }
            .orElseThrow { RuntimeException("Passenger with id=$id not found") }
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<PassengerResponse> {
        return passengerRepository.findAll(pageable)
            .map { p -> p.toResponse() }
    }

    override fun update(id: Int, request: PassengerUpdate): PassengerResponse {
        val entity = passengerRepository.findById(id)
            .orElseThrow { RuntimeException("Passenger with id=$id not found") }
        entity.mapIfNotNull(request)
        passengerRepository.save(entity)
        return entity.toResponse()
    }

    override fun deleteById(id: Int) {
        return passengerRepository.deleteById(id)
    }

    override fun create(request: PassengerCreate): PassengerResponse {
        val entity = request.toEntity()
        passengerRepository.save(entity)
        return entity.toResponse()
    }
}