package com.sivalabs.devzone.users.domain

data class UserDTO(
    var id: Long,
    var name: String,
    var email: String,
    var password: String,
    var role: RoleEnum,
) {
    companion object {
        fun fromEntity(user: User): UserDTO {
            val dto = UserDTO(user.id!!, user.name, user.email, user.password, user.role)
            return dto
        }
    }
}
