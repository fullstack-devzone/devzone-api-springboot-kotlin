package com.sivalabs.devzone.security

import com.sivalabs.devzone.users.domain.User
import com.sivalabs.devzone.users.domain.UserService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

@Component
class SecurityUtils(private val userService: UserService) {
    fun loginUser(): User? {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        if (authentication.principal == null) {
            return null
        }
        if (authentication.principal is SecurityUser) {
            val securityUser = authentication.principal as SecurityUser
            return securityUser.user
        } else if (authentication is UsernamePasswordAuthenticationToken) {
            if (authentication.principal is UserDetails) {
                val userDetails: UserDetails = authentication.principal as UserDetails
                return userService.getUserByEmail(userDetails.username)
            }
            if (authentication.principal is String) {
                val username = authentication.principal as String
                return userService.getUserByEmail(username)
            }
        }
        return null
    }
}
