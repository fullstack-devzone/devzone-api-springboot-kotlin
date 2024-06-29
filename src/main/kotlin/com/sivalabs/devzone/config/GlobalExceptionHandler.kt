package com.sivalabs.devzone.config

import com.sivalabs.devzone.common.exceptions.BadRequestException
import com.sivalabs.devzone.common.exceptions.DevZoneException
import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException
import com.sivalabs.devzone.common.exceptions.UnauthorisedAccessException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.net.URI

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    private val log = KotlinLogging.logger {}

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(e: ResourceNotFoundException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)
        problemDetail.title = "Resource Not Found"
        problemDetail.type = URI.create("https://api.devzone.com/errors/not-found")
        return problemDetail
    }

    @ExceptionHandler(DevZoneException::class)
    fun handleDevZoneException(e: DevZoneException): ProblemDetail {
        log.error(e) { e.localizedMessage }
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.message!!)
        problemDetail.title = "Unknown Problem"
        problemDetail.type = URI.create("https://api.devzone.com/errors/unknown")
        return problemDetail
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(e: BadRequestException): ProblemDetail {
        log.error(e) { e.localizedMessage }
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message!!)
        problemDetail.title = "Unknown Problem"
        problemDetail.type = URI.create("https://api.devzone.com/errors/badrequest")
        return problemDetail
    }

    @ExceptionHandler(UnauthorisedAccessException::class)
    fun handleUnauthorisedAccessException(e: UnauthorisedAccessException): ProblemDetail {
        log.error(e) { e.localizedMessage }
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.message!!)
        problemDetail.title = "Forbidden"
        problemDetail.type = URI.create("https://api.devzone.com/errors/forbidden")
        return problemDetail
    }
}
