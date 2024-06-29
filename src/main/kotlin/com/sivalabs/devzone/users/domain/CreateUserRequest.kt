package com.sivalabs.devzone.users.domain

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class CreateUserRequest(
    @field:NotBlank(message = "Name cannot be blank")
    val name: String,
    @field:NotBlank(message = "Email cannot be blank")
    @field:Email(message = "Invalid email address")
    val email: String,
    @field:NotBlank(message = "Password cannot be blank")
    val password: String,
)
