package com.sivalabs.devzone.config.security

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class TokenAuthenticationFilter(
    private val tokenHelper: TokenHelper,
    private val userDetailsService: UserDetailsService,
) : OncePerRequestFilter() {
    @Throws(IOException::class, ServletException::class)
    public override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
    ) {
        val authToken = tokenHelper.getToken(request)
        if (authToken != null) {
            val username = tokenHelper.getUsernameFromToken(authToken)
            if (username != null) {
                val userDetails = userDetailsService.loadUserByUsername(username)
                if (tokenHelper.validateToken(authToken, userDetails)) {
                    val authentication = TokenBasedAuthentication(authToken, userDetails)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        }
        chain.doFilter(request, response)
    }
}
