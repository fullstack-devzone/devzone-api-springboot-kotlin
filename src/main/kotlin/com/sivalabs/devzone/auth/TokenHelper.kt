package com.sivalabs.devzone.auth

import com.sivalabs.devzone.ApplicationProperties
import com.sivalabs.devzone.common.exceptions.DevZoneException
import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Date

@Component
class TokenHelper(private val properties: ApplicationProperties) {
    private val log = KotlinLogging.logger {}

    fun getUsernameFromToken(token: String): String? {
        try {
            val claims: Claims = getAllClaimsFromToken(token)
            return claims.subject
        } catch (e: Exception) {
            log.error(e) { "${e.message}" }
            return null
        }
    }

    fun generateToken(username: String): String {
        val secretString = properties.jwt.secret
        val key = Keys.hmacShaKeyFor(secretString.toByteArray(StandardCharsets.UTF_8))

        return Jwts.builder()
            .issuer(properties.jwt.issuer)
            .subject(username)
            .issuedAt(Date())
            .expiration(generateExpirationDate())
            .signWith(key)
            .compact()
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        val secretString = properties.jwt.secret
        val key = Keys.hmacShaKeyFor(secretString.toByteArray(StandardCharsets.UTF_8))

        val claims: Claims =
            try {
                Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .payload
            } catch (e: Exception) {
                throw DevZoneException(e)
            }
        return claims
    }

    private fun generateExpirationDate(): Date {
        return Date(System.currentTimeMillis() + properties.jwt.expiresIn * 1000)
    }

    fun validateToken(
        token: String,
        userDetails: UserDetails,
    ): Boolean {
        val username = getUsernameFromToken(token)
        return username != null && username == userDetails.username
    }

    fun getToken(request: HttpServletRequest): String? {
        val authHeader = request.getHeader(properties.jwt.header)
        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader.substring(7)
        } else {
            null
        }
    }
}
