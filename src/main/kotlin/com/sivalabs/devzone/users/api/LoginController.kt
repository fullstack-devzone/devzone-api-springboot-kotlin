package com.sivalabs.devzone.users.api

import com.sivalabs.devzone.ApplicationProperties
import com.sivalabs.devzone.security.SecurityUser
import com.sivalabs.devzone.security.TokenHelper
import com.sivalabs.devzone.users.domain.AuthUserDTO
import com.sivalabs.devzone.users.domain.LoginRequest
import com.sivalabs.devzone.users.domain.LoginResponse
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
    private val authenticationManager: AuthenticationManager,
    private val tokenHelper: TokenHelper,
    private val applicationProperties: ApplicationProperties,
) {
    @PostMapping("/login")
    fun login(
        @RequestBody @Valid
        credentials: LoginRequest,
    ): ResponseEntity<LoginResponse> {
        return try {
            val authentication =
                authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                        credentials.username,
                        credentials.password,
                    ),
                )
            SecurityContextHolder.getContext().authentication = authentication
            val user: SecurityUser = authentication.principal as SecurityUser
            val accessToken: String = tokenHelper.generateToken(user.username)
            ResponseEntity.ok(getAuthenticationResponse(user, accessToken))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    private fun getAuthenticationResponse(
        securityUser: SecurityUser,
        token: String,
    ): LoginResponse {
        val authUserDTO = AuthUserDTO(securityUser.user.name!!, securityUser.user.email!!, securityUser.user.role!!)
        return LoginResponse(
            token,
            LocalDateTime.now().plusSeconds(applicationProperties.jwt.expiresIn),
            authUserDTO,
        )
    }
}