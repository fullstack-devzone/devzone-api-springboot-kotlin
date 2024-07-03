package com.sivalabs.devzone.auth

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails

class JwtAuthToken(private val token: String, private val principle: UserDetails) : AbstractAuthenticationToken(
    principle.authorities,
) {
    override fun isAuthenticated() = true

    override fun getCredentials() = token

    override fun getPrincipal() = principle
}
