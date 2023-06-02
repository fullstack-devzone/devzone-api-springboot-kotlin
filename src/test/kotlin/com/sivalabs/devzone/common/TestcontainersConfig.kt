package com.sivalabs.devzone.common

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
    fun postgreSQLContainer(): PostgreSQLContainer<*> {
        return PostgreSQLContainer(TestConstants.POSTGRES_IMAGE)
    }
}
