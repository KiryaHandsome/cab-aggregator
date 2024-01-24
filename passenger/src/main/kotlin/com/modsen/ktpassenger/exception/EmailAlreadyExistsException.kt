package com.modsen.ktpassenger.exception

import org.springframework.http.HttpStatus


class EmailAlreadyExistsException(message: String, email: String) :
    BaseException(HttpStatus.CONFLICT.value(), message, email)

