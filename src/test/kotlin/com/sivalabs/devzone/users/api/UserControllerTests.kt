package com.sivalabs.devzone.users.api

import com.sivalabs.devzone.BaseIT
import com.sivalabs.devzone.TestConstants.ADMIN_EMAIL
import com.sivalabs.devzone.users.domain.CreateUserCmd
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.instancio.Instancio
import org.junit.jupiter.api.Test

class UserControllerTests : BaseIT() {
    @Test
    fun `should find User by Id`() {
        val userId = 1L
        given().get("/api/users/{id}", userId)
            .then().statusCode(200)
    }

    @Test
    fun `should create new user with valid data`() {
        val request = Instancio.of(CreateUserCmd::class.java).create()
        given().contentType(ContentType.JSON)
            .body(request)
            .post("/api/users")
            .then()
            .statusCode(201)
    }

    @Test
    fun `should fail to create new user with existing email`() {
        val request = CreateUserCmd("myname", ADMIN_EMAIL, "secret")
        given().contentType(ContentType.JSON)
            .body(request)
            .post("/api/users")
            .then()
            .statusCode(400)
    }
}
