package com.sivalabs.devzone.config.security

import com.sivalabs.devzone.common.exceptions.ApplicationException
import com.sivalabs.devzone.common.logging.logger
import com.sivalabs.devzone.config.ApplicationProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class TokenHelper(private val applicationProperties: ApplicationProperties) {
    companion object {
        private val log = logger()
        private val SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512
        private const val AUDIENCE_WEB = "web"
    }

    fun getUsernameFromToken(token: String): String? {
        try {
            val claims: Claims = getAllClaimsFromToken(token)
            return claims.subject
        } catch (e: Exception) {
            log.error(e.message, e)
        }
        return null
    }

    fun generateToken(username: String): String {
        return Jwts.builder()
            .setIssuer(applicationProperties.jwt.issuer)
            .setSubject(username)
            .setAudience(AUDIENCE_WEB)
            .setIssuedAt(Date())
            .setExpiration(generateExpirationDate())
            .signWith(SIGNATURE_ALGORITHM, applicationProperties.jwt.secret)
            .compact()
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        val claims: Claims = try {
            Jwts.parser()
                .setSigningKey(applicationProperties.jwt.secret)
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            throw ApplicationException(e)
        }
        return claims
    }

    private fun generateExpirationDate(): Date {
        return Date(System.currentTimeMillis() + applicationProperties.jwt.expiresIn * 1000)
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = getUsernameFromToken(token)
        return username != null && username == userDetails.username
    }

    fun getToken(request: HttpServletRequest): String? {
        val authHeader = getAuthHeaderFromHeader(request)
        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader.substring(7)
        } else null
    }

    fun getAuthHeaderFromHeader(request: HttpServletRequest): String? {
        return request.getHeader(applicationProperties.jwt.header)
    }
}
