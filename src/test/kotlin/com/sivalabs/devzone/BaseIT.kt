package com.sivalabs.devzone

import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(
    TestcontainersConfig::class,
)
abstract class BaseIT {
    @LocalServerPort
    var port = 0

    @Autowired
    protected lateinit var properties: ApplicationProperties

    @BeforeEach
    fun setUpBase() {
        RestAssured.port = port
    }
}
