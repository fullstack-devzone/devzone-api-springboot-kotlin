package com.sivalabs.devzone.auth

import com.sivalabs.devzone.common.exceptions.UnauthorisedAccessException
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
        return if (authentication.principal is SecurityUser) {
            authentication.principal as SecurityUser
        } else {
            null
        }
    }

    fun getRequiredLoginUser(): SecurityUser {
        return loginUser() ?: throw UnauthorisedAccessException("Unauthorised access")
    }
}
