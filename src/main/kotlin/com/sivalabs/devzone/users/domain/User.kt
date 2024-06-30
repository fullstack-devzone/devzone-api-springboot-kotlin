package com.sivalabs.devzone.users.domain

import java.time.LocalDateTime

class User {
    var id: Long? = null
    var name: String = ""
    var email: String = ""
    var password: String = ""
    var role: RoleEnum = RoleEnum.ROLE_USER
    var createdAt = LocalDateTime.now()
    var updatedAt = LocalDateTime.now()

    fun isCurrentUserAdmin(): Boolean {
        return isUserHasAnyRole(RoleEnum.ROLE_ADMIN)
    }

    private fun isUserHasAnyRole(vararg roles: RoleEnum): Boolean {
        return listOf(*roles).contains(this.role)
    }
}
