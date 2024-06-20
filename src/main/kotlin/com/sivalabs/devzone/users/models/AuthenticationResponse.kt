package com.sivalabs.devzone.users.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class AuthenticationResponse(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("expires_at")
    val expiresAt: LocalDateTime,
    @JsonProperty("user")
    val user: AuthUserDTO,
)
