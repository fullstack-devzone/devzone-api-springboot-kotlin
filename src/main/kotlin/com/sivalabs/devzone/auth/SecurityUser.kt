package com.sivalabs.devzone.auth

import com.sivalabs.devzone.users.domain.RoleEnum
import org.springframework.security.core.authority.SimpleGrantedAuthority

class SecurityUser(
    val id: Long,
    val name: String,
    val email: String,
    password: String,
    val role: RoleEnum,
) : org.springframework.security.core.userdetails.User(
        email,
        password,
        setOf(SimpleGrantedAuthority(role.name)),
    ) {
    fun isCurrentUserAdmin(): Boolean {
        return isUserHasAnyRole(RoleEnum.ROLE_ADMIN)
    }

    private fun isUserHasAnyRole(vararg roles: RoleEnum): Boolean {
        return listOf(*roles).contains(this.role)
    }
}
