package com.modsen.ktpassenger.exception

import org.springframework.http.HttpStatus

class PassengerNotFoundException(override val errorMessage: String, id: Int) :
    BaseException(HttpStatus.NOT_FOUND.value(), errorMessage, id)