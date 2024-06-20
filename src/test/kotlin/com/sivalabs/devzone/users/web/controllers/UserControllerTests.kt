package com.sivalabs.devzone.users.web.controllers

import com.sivalabs.devzone.common.AbstractIntegrationTest
import com.sivalabs.devzone.common.TestConstants.ADMIN_EMAIL
import com.sivalabs.devzone.users.models.CreateUserRequest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.instancio.Instancio
import org.junit.jupiter.api.Test

class UserControllerTests : AbstractIntegrationTest() {
    @Test
    fun shouldFindUserById() {
        val userId = 1L
        given().get("/api/users/{id}", userId)
            .then().statusCode(200)
    }

    @Test
    fun shouldCreateNewUserWithValidData() {
        val request = Instancio.of(CreateUserRequest::class.java).create()
        given().contentType(ContentType.JSON)
            .body(request)
            .post("/api/users")
            .then()
            .statusCode(201)
    }

    @Test
    fun shouldFailToCreateNewUserWithExistingEmail() {
        val request = CreateUserRequest("myname", ADMIN_EMAIL, "secret")
        given().contentType(ContentType.JSON)
            .body(request)
            .post("/api/users")
            .then()
            .statusCode(400)
    }
}
