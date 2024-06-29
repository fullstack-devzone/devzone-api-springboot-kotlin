package com.sivalabs.devzone.users.domain

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "UserName cannot be blank")
    val username: String?,
    @field:NotBlank(message = "Password cannot be blank")
    val password: String?,
)
