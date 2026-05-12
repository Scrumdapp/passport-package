package com.scrumdapp.passportstarter.configs

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "passport")
data class PassportProperties(
    var jwkSetUri: String = ""
)
