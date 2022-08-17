package com.sivalabs.devzone.config.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails

class TokenBasedAuthentication(
    private val token: String,
    private val principle: UserDetails
) : AbstractAuthenticationToken(principle.authorities) {

    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun getCredentials(): Any {
        return token
    }

    override fun getPrincipal(): Any {
        return principle
    }
}
