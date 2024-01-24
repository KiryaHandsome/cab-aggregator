package com.modsen.ktpassenger.controller

import com.modsen.ktpassenger.dto.PassengerCreate
import com.modsen.ktpassenger.dto.PassengerResponse
import com.modsen.ktpassenger.dto.PassengerUpdate
import com.modsen.ktpassenger.service.PassengerService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/passengers")
class PassengerController(
    private val passengerService: PassengerService
) {

    @GetMapping("/{id}")
    fun getPassenger(@PathVariable id: Int): PassengerResponse = passengerService.findById(id)

    @GetMapping
    fun getAllPassengers(pageable: Pageable): Page<PassengerResponse> = passengerService.findAll(pageable)

    @PatchMapping("/{id}")
    fun updatePassenger(@PathVariable id: Int, @Valid @RequestBody request: PassengerUpdate): PassengerResponse =
        passengerService.update(id, request)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPassenger(@Valid @RequestBody request: PassengerCreate): PassengerResponse =
        passengerService.create(request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletePassengerById(@PathVariable id: Int) {
        passengerService.deleteById(id)
    }
}