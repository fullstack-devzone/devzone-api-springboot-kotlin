package com.sivalabs.devzone.auth.api

import com.sivalabs.devzone.ApplicationProperties
import com.sivalabs.devzone.auth.SecurityUser
import com.sivalabs.devzone.auth.TokenHelper
import com.sivalabs.devzone.users.domain.LoginRequest
import com.sivalabs.devzone.users.domain.LoginResponse
import com.sivalabs.devzone.users.domain.UserDTO
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api")
class LoginController(
    private val authManager: AuthenticationManager,
    private val tokenHelper: TokenHelper,
    private val properties: ApplicationProperties,
) {
    @PostMapping("/login")
    fun login(
        @RequestBody @Valid
        credentials: LoginRequest,
    ): ResponseEntity<LoginResponse> {
        return try {
            val authentication =
                authManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                        credentials.username,
                        credentials.password,
                    ),
                )
            SecurityContextHolder.getContext().authentication = authentication
            val user: SecurityUser = authentication.principal as SecurityUser
            val accessToken: String = tokenHelper.generateToken(user.username)
            ResponseEntity.ok(getLoginResponse(user, accessToken))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    private fun getLoginResponse(
        securityUser: SecurityUser,
        token: String,
    ): LoginResponse {
        val userDTO =
            UserDTO(
                securityUser.id,
                securityUser.name,
                securityUser.email,
                securityUser.role,
            )
        return LoginResponse(
            token,
            LocalDateTime.now().plusSeconds(properties.jwt.expiresIn),
            userDTO,
        )
    }
}
