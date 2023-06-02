package com.sivalabs.devzone.users.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.sivalabs.devzone.users.entities.RoleEnum
import com.sivalabs.devzone.users.entities.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

class UserDTO {
    var id: Long? = null

    @NotBlank(message = "Name cannot be blank")
    var name: String? = null

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email address")
    var email: String? = null

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password cannot be blank")
    var password: String? = null

    var role: RoleEnum? = null

    companion object {
        fun fromEntity(user: User): UserDTO {
            val dto = UserDTO()
            dto.id = (user.id)
            dto.name = (user.name)
            dto.email = (user.email)
            dto.password = (user.password)
            dto.role = (user.role)
            return dto
        }
    }
}
