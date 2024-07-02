package com.sivalabs.devzone.auth.api

import com.sivalabs.devzone.auth.SecurityUtils
import com.sivalabs.devzone.users.domain.UserDTO
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
class CurrentUserController(
    private val securityUtils: SecurityUtils,
) {
    @GetMapping("/me")
    fun me(): ResponseEntity<UserDTO> {
        val loginUser = securityUtils.loginUser()
        if (loginUser != null) {
            val userDTO = UserDTO(loginUser.id!!, loginUser.name, loginUser.email, loginUser.role)
            return ResponseEntity.ok(userDTO)
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }
}
