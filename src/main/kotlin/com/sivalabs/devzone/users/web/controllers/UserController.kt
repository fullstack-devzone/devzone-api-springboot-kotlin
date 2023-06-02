package com.sivalabs.devzone.users.web.controllers

import com.sivalabs.devzone.common.logging.logger
import com.sivalabs.devzone.users.models.CreateUserRequest
import com.sivalabs.devzone.users.models.UserDTO
import com.sivalabs.devzone.users.services.UserService
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
    companion object {
        private val log = logger()
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<UserDTO> {
        log.info("process=get_user, user_id={}", id)
        return userService
            .getUserById(id)
            .map { body: UserDTO? -> ResponseEntity.ok(body) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody @Valid createUserRequest: CreateUserRequest): UserDTO {
        log.info("process=create_user, user_email={}", createUserRequest.email)
        return userService.createUser(createUserRequest)
    }
}
