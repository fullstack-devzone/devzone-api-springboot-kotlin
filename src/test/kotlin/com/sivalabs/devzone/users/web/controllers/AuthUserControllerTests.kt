package com.sivalabs.devzone.users.web.controllers

import com.sivalabs.devzone.common.AbstractIntegrationTest
import com.sivalabs.devzone.common.TestConstants.ADMIN_EMAIL
import com.sivalabs.devzone.config.security.TokenHelper
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class AuthUserControllerTests : AbstractIntegrationTest() {
    @Autowired
    private lateinit var tokenHelper: TokenHelper

    @Test
    fun shouldGetLoginUserDetails() {
        val jwtToken = tokenHelper.generateToken(ADMIN_EMAIL)
        given()
            .header(properties.jwt.header, "Bearer $jwtToken")
            .get("/api/me")
            .then()
            .statusCode(200)
    }

    @Test
    fun shouldFailToGetLoginUserDetailsIfUnauthorized() {
        given().get("/api/me")
            .then().statusCode(401)
    }
}
