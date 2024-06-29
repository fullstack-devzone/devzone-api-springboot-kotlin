package com.sivalabs.devzone.users.domain

data class AuthUserDTO(
    val name: String,
    val email: String,
    val role: RoleEnum,
)
