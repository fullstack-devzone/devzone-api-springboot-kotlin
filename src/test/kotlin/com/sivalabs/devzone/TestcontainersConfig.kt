package com.sivalabs.devzone

import org.springframework.boot.devtools.restart.RestartScope
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfig {
    @Bean
    @ServiceConnection
    @RestartScope
    fun postgresContainer(): PostgreSQLContainer<*> {
        return PostgreSQLContainer("postgres:16-alpine")
    }
}
