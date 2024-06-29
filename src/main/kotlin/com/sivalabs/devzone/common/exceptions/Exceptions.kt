package com.sivalabs.devzone.common.exceptions

open class DevZoneException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

class BadRequestException(message: String) : DevZoneException(message)

class ResourceNotFoundException(message: String) : DevZoneException(message)

class UnauthorisedAccessException(message: String) : DevZoneException(message)
