package com.sivalabs.devzone.config

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {

    @Bean
    fun javaTimeModule(): JavaTimeModule {
        return JavaTimeModule()
    }
}
