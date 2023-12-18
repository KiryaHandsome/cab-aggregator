package com.modsen.ktpassenger.exception

open class BaseException(val statusCode: Int, open val errorMessage: String) : RuntimeException(errorMessage)