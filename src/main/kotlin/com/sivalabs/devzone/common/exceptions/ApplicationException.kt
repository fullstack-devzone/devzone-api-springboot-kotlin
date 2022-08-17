package com.sivalabs.devzone.common.exceptions

open class ApplicationException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}
