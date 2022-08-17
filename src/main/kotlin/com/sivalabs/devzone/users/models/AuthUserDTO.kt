package com.sivalabs.devzone.users.models

import com.sivalabs.devzone.users.entities.RoleEnum

data class AuthUserDTO(
    val name: String,
    val email: String,
    val role: RoleEnum
)
