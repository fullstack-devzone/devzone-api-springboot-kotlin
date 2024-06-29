package com.sivalabs.devzone.security

import com.sivalabs.devzone.common.logging.logger
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class TokenAuthFilter(
    private val tokenHelper: TokenHelper,
    private val userDetailsService: UserDetailsService,
) : OncePerRequestFilter() {
    companion object {
        private val log = logger()
    }

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
                        val authentication =
                            UsernamePasswordAuthenticationToken(userDetails.username, userDetails.password, userDetails.authorities)
                        SecurityContextHolder.getContext().authentication = authentication
                    }
                }
            } catch (e: RuntimeException) {
                log.error("Error processing access_token", e)
            }
        }
        chain.doFilter(request, response)
    }
}
