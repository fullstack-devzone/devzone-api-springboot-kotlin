package com.sivalabs.devzone.users.web.controllers

import com.sivalabs.devzone.common.AbstractIntegrationTest
import com.sivalabs.devzone.users.entities.User
import com.sivalabs.devzone.users.models.AuthenticationRequest
import com.sivalabs.devzone.users.models.CreateUserRequest
import com.sivalabs.devzone.users.services.UserService
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

class AuthenticationControllerTests : AbstractIntegrationTest() {

    @Autowired
    private lateinit var userService: UserService

    @Test
    fun shouldLoginSuccessfullyWithValidCredentials() {
        val user = createUser()
        val authenticationRequest = AuthenticationRequest(user.email, user.password)
        RestAssured.given().contentType(ContentType.JSON)
            .body(authenticationRequest)
            .post("/api/login")
            .then()
            .statusCode(200)
    }

    @Test
    fun shouldNotLoginWithInvalidCredentials() {
        val authenticationRequest = AuthenticationRequest("nonexisting@gmail.com", "secret")
        RestAssured.given().contentType(ContentType.JSON)
            .body(authenticationRequest)
            .post("/api/login")
            .then()
            .statusCode(401)
    }

    private fun createUser(): User {
        val uuid = UUID.randomUUID().toString()
        val request = CreateUserRequest(uuid, "$uuid@gmail.com", uuid)
        val userDTO = userService.createUser(request)
        val user = User()
        user.id = userDTO.id
        user.name = userDTO.name
        user.email = userDTO.email
        user.password = request.password
        user.role = userDTO.role
        return user
    }
}
