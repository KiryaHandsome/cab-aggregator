package com.modsen.ktpassenger.exception

import org.springframework.http.HttpStatus

class PhoneNumberAlreadyExistsException(message: String, phoneNumber: String) :
    BaseException(HttpStatus.CONFLICT.value(), message, phoneNumber)