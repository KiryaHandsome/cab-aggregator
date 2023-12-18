package com.modsen.ktpassenger.exception

import org.springframework.http.HttpStatus

class NotFoundException(override val errorMessage: String) : BaseException(HttpStatus.NOT_FOUND.value(), errorMessage)