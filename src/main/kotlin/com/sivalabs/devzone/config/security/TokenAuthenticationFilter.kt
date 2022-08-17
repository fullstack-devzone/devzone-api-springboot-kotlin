package com.sivalabs.devzone.config.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class TokenAuthenticationFilter(
    private val tokenHelper: TokenHelper,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    @Throws(IOException::class, ServletException::class)
    public override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
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
