package com.sivalabs.devzone

import com.sivalabs.devzone.common.TestcontainersConfig
import org.springframework.boot.SpringApplication
import com.sivalabs.devzone.main as applicationMain

object TestApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.from(::applicationMain)
            .with(TestcontainersConfig::class.java)
            .run(*args)
    }
}
