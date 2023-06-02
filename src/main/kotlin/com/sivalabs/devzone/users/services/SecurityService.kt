package com.sivalabs.devzone.users.services

import com.sivalabs.devzone.config.security.SecurityUser
import com.sivalabs.devzone.users.entities.User
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SecurityService(private val userService: UserService) {
    fun loginUser(): User? {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null && authentication.principal is SecurityUser) {
            return (authentication.principal as SecurityUser).user
        } else if (authentication is UsernamePasswordAuthenticationToken) {
            val userDetails = authentication.getPrincipal() as UserDetails
            return userService.getUserByEmail(userDetails.username).orElse(null)
        }
        return null
    }
}
