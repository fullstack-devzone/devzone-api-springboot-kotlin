package com.sivalabs.devzone.users.web.controllers

import com.sivalabs.devzone.common.annotations.AnyAuthenticatedUser
import com.sivalabs.devzone.config.security.SecurityUtils
import com.sivalabs.devzone.users.models.AuthUserDTO
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
class AuthUserController(
    private val securityUtils: SecurityUtils,
) {
    @GetMapping("/me")
    @AnyAuthenticatedUser
    fun me(): ResponseEntity<AuthUserDTO> {
        val loginUser = securityUtils.loginUser()
        if (loginUser != null) {
            val userDTO = AuthUserDTO(loginUser.name!!, loginUser.email!!, loginUser.role!!)
            return ResponseEntity.ok(userDTO)
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }
}
