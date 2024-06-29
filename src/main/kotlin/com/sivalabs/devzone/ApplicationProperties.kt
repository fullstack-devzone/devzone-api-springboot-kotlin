package com.sivalabs.devzone

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "devzone")
class ApplicationProperties {
    var importDataEnabled = true
    var importFilePath = ""
    var jwt = JwtConfig()

    class JwtConfig {
        var issuer = "DevZone"
        var header = "Authorization"
        var expiresIn = 604800L
        var secret = ""
    }
}
