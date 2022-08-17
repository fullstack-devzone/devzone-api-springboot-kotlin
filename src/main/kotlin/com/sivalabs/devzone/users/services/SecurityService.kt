package com.sivalabs.devzone.users.services

import com.sivalabs.devzone.config.security.SecurityUser
import com.sivalabs.devzone.users.entities.RoleEnum
import com.sivalabs.devzone.users.models.UserDTO
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SecurityService(private val userService: UserService) {
    fun loginUser(): UserDTO? {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null && authentication.principal is SecurityUser) {
            val securityUser: SecurityUser = authentication.principal as SecurityUser
            return securityUser.user
        } else if (authentication is UsernamePasswordAuthenticationToken) {
            val userDetails = authentication.getPrincipal() as UserDetails
            return userService.getUserByEmail(userDetails.username).orElse(null)
        }
        return null
    }

    fun isCurrentUserAdmin(): Boolean {
        return isUserHasAnyRole(loginUser(), RoleEnum.ROLE_ADMIN)
    }

    private fun isUserHasAnyRole(loginUser: UserDTO?, vararg roles: RoleEnum): Boolean {
        return listOf(*roles).contains(loginUser!!.role)
    }
}
