package com.sivalabs.devzone.auth

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class TokenAuthFilter(
    private val tokenHelper: TokenHelper,
    private val userDetailsService: UserDetailsService,
) : OncePerRequestFilter() {
    private val log = KotlinLogging.logger {}

    public override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
    ) {
        val authToken = tokenHelper.getToken(request)
        if (authToken != null) {
            try {
                val username = tokenHelper.getUsernameFromToken(authToken)
                if (username != null) {
                    val userDetails = userDetailsService.loadUserByUsername(username)
                    if (tokenHelper.validateToken(authToken, userDetails)) {
                        val authentication = TokenBasedAuthentication(authToken, userDetails)
                        SecurityContextHolder.getContext().authentication = authentication
                    }
                }
            } catch (e: RuntimeException) {
                log.error(e) { "Error processing access_token" }
            }
        }
        chain.doFilter(request, response)
    }
}
