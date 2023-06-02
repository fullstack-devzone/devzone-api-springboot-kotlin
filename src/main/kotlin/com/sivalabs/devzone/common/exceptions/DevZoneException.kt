package com.sivalabs.devzone.common.exceptions

open class DevZoneException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}
