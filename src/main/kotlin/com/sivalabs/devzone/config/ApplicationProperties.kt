package com.sivalabs.devzone.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "devzone")
class ApplicationProperties {
    var importDataEnabled = true
    var importFilePath: String = ""
    var jwt = JwtConfig()

    class JwtConfig {
        var issuer = "DevZone"
        var header = "Authorization"
        var expiresIn = DEFAULT_JWT_TOKEN_EXPIRES
        var secret = ""

        companion object {
            private const val DEFAULT_JWT_TOKEN_EXPIRES = 604800L
        }
    }
}
