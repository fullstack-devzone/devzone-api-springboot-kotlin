package com.sivalabs.devzone.config.security

import com.sivalabs.devzone.common.exceptions.DevZoneException
import com.sivalabs.devzone.common.logging.logger
import com.sivalabs.devzone.config.ApplicationProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Date

@Component
class TokenHelper(private val applicationProperties: ApplicationProperties) {
    companion object {
        private val log = logger()
        private val SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256
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
        val secretString = applicationProperties.jwt.secret
        val key = Keys.hmacShaKeyFor(secretString.toByteArray(StandardCharsets.UTF_8))

        return Jwts.builder()
            .setIssuer(applicationProperties.jwt.issuer)
            .setSubject(username)
            .setAudience(AUDIENCE_WEB)
            .setIssuedAt(Date())
            .setExpiration(generateExpirationDate())
            .signWith(key, SIGNATURE_ALGORITHM)
            .compact()
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        val secretString = applicationProperties.jwt.secret
        val key = Keys.hmacShaKeyFor(secretString.toByteArray(StandardCharsets.UTF_8))

        val claims: Claims =
            try {
                Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .body
            } catch (e: Exception) {
                throw DevZoneException(e)
            }
        return claims
    }

    private fun generateExpirationDate(): Date {
        return Date(System.currentTimeMillis() + applicationProperties.jwt.expiresIn * 1000)
    }

    fun validateToken(
        token: String,
        userDetails: UserDetails,
    ): Boolean {
        val username = getUsernameFromToken(token)
        return username != null && username == userDetails.username
    }

    fun getToken(request: HttpServletRequest): String? {
        val authHeader = getAuthHeaderFromHeader(request)
        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader.substring(7)
        } else {
            null
        }
    }

    fun getAuthHeaderFromHeader(request: HttpServletRequest): String? {
        return request.getHeader(applicationProperties.jwt.header)
    }
}
