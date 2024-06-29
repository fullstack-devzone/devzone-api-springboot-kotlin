package com.sivalabs.devzone.users.api

import com.sivalabs.devzone.common.BaseIT
import com.sivalabs.devzone.common.TestConstants.ADMIN_EMAIL
import com.sivalabs.devzone.security.TokenHelper
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class CurrentUserControllerTests : BaseIT() {
    @Autowired
    private lateinit var tokenHelper: TokenHelper

    @Test
    fun `should get login user details`() {
        val jwtToken = tokenHelper.generateToken(ADMIN_EMAIL)
        given()
            .header(properties.jwt.header, "Bearer $jwtToken")
            .get("/api/me")
            .then()
            .statusCode(200)
    }

    @Test
    fun `should fail to get login user details if unauthorized`() {
        given().get("/api/me")
            .then().statusCode(403)
    }
}
