package com.sivalabs.devzone.users.api

import com.sivalabs.devzone.users.domain.CreateUserRequest
import com.sivalabs.devzone.users.domain.UserDTO
import com.sivalabs.devzone.users.domain.UserService
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
) {
    private val log = KotlinLogging.logger {}

    @GetMapping("/{id}")
    fun getUser(
        @PathVariable id: Long,
    ): ResponseEntity<UserDTO> {
        log.info { "process=get_user, user_id=$id" }
        return userService
            .getUserById(id)
            ?.let { userDTO -> ResponseEntity.ok(userDTO) }
            ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(
        @RequestBody @Valid request: CreateUserRequest,
    ): UserDTO {
        log.info { "process=create_user, user_email=${request.email}" }
        return userService.createUser(request)
    }
}
