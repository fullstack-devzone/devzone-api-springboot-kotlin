package com.sivalabs.devzone

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    properties = [
        "spring.datasource.url=jdbc:tc:postgresql:16-alpine:///testdb",
    ],
)
class ApplicationTests {
    @Test
    fun contextLoads() {
    }
}
