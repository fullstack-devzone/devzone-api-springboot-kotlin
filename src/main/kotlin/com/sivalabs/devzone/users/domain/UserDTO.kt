package com.sivalabs.devzone.users.domain

data class UserDTO(
    var id: Long,
    var name: String,
    var email: String,
    var role: RoleEnum,
)
