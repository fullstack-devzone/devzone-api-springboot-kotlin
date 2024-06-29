package com.sivalabs.devzone

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.EnableAspectJAutoProxy

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties::class)
@EnableAspectJAutoProxy
@EnableCaching
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
