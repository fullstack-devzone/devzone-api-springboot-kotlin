package com.sivalabs.devzone

import org.springframework.boot.fromApplication
import org.springframework.boot.with

fun main(args: Array<String>) {
    fromApplication<Application>()
        .with(TestcontainersConfig::class)
        .run(*args)
}
