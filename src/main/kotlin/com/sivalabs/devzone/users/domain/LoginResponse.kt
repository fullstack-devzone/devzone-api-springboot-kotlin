package com.sivalabs.devzone.users.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class LoginResponse(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("expires_at")
    val expiresAt: LocalDateTime,
    @JsonProperty("user")
    val user: UserDTO,
)
