package com.sivalabs.devzone.users.domain

data class UserDetailsDTO(
    var id: Long,
    var name: String,
    var email: String,
    var password: String,
    var role: RoleEnum,
)
