package com.sivalabs.devzone.auth

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SecurityUtils {
    fun loginUser(): SecurityUser? {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        if (authentication.principal == null) {
            return null
        }
        if (authentication.principal is SecurityUser) {
            return authentication.principal as SecurityUser
        }
        return null
    }
}
