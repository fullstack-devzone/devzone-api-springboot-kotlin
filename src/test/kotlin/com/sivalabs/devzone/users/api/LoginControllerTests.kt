package com.sivalabs.devzone.users.api

import com.sivalabs.devzone.common.BaseIT
import com.sivalabs.devzone.users.domain.CreateUserRequest
import com.sivalabs.devzone.users.domain.LoginRequest
import com.sivalabs.devzone.users.domain.UserService
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

class LoginControllerTests : BaseIT() {
    @Autowired
    private lateinit var userService: UserService

    @Test
    fun `should login successfully with valid credentials`() {
        val uuid = UUID.randomUUID().toString()
        val request = CreateUserRequest(uuid, "$uuid@gmail.com", uuid)
        val userDTO = userService.createUser(request)
        val loginRequest = LoginRequest(userDTO.email, request.password)
        given().contentType(ContentType.JSON)
            .body(loginRequest)
            .post("/api/login")
            .then()
            .statusCode(200)
    }

    @Test
    fun `should not login with invalid credentials`() {
        val loginRequest = LoginRequest("nonexisting@gmail.com", "secret")
        given().contentType(ContentType.JSON)
            .body(loginRequest)
            .post("/api/login")
            .then()
            .statusCode(401)
    }
}
