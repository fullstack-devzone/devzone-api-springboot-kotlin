package com.sivalabs.devzone.users.models

import jakarta.validation.constraints.NotBlank

data class AuthenticationRequest(
    @field:NotBlank(message = "UserName cannot be blank")
    val username: String?,
    @field:NotBlank(message = "Password cannot be blank")
    val password: String?,
)
