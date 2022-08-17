package com.sivalabs.devzone.config.advice

import com.sivalabs.devzone.common.exceptions.ApplicationException
import com.sivalabs.devzone.common.exceptions.BadRequestException
import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException
import com.sivalabs.devzone.common.logging.logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.NativeWebRequest
import org.zalando.problem.Problem
import org.zalando.problem.Status
import org.zalando.problem.spring.web.advice.ProblemHandling
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait

@RestControllerAdvice
class GlobalExceptionHandler : ProblemHandling, SecurityAdviceTrait {
    companion object {
        private val log = logger()
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(
        exception: ResourceNotFoundException,
        request: NativeWebRequest
    ): ResponseEntity<Problem> {
        log.error(exception.localizedMessage, exception)
        return create(Status.NOT_FOUND, exception, request)
    }

    @ExceptionHandler(ApplicationException::class)
    fun handleApplicationException(
        exception: ApplicationException,
        request: NativeWebRequest
    ): ResponseEntity<Problem> {
        log.error(exception.localizedMessage, exception)
        return create(Status.BAD_REQUEST, exception, request)
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(
        exception: BadRequestException,
        request: NativeWebRequest
    ): ResponseEntity<Problem> {
        log.error(exception.localizedMessage, exception)
        return create(Status.BAD_REQUEST, exception, request)
    }
}
