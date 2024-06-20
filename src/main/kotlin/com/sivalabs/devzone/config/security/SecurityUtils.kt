package com.sivalabs.devzone.config.security

import com.sivalabs.devzone.users.entities.User
import com.sivalabs.devzone.users.services.UserService
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
            val userDetails: UserDetails = authentication.principal as UserDetails
            return userService.getUserByEmail(userDetails.username).orElse(null)
        }
        return null
    }
}
