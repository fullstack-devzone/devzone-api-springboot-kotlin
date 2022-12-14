package com.sivalabs.devzone.users.models

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class CreateUserRequest(
    @field:NotBlank(message = "Name cannot be blank")
    val name: String?,

    @field:NotBlank(message = "Email cannot be blank")
    @field:Email(message = "Invalid email address")
    val email: String?,

    @field:NotBlank(message = "Password cannot be blank")
    val password: String?
)
