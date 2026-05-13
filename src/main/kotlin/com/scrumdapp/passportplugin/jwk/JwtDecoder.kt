package com.scrumdapp.passportplugin.jwk

import com.scrumdapp.passportplugin.PassportProperties
import org.springframework.context.annotation.Bean
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder

@Bean
fun jwtDecoder(
    passportProperties: PassportProperties
): JwtDecoder {
    val uri = passportProperties.jwkSetUri
    if (uri == "") {
        throw IllegalArgumentException("No jwkSetUri provided, please provide a valid jwkSetUri using passport.jwkSetUri")
    }

    val decoder = NimbusJwtDecoder
        .withJwkSetUri(passportProperties.jwkSetUri)
        .build()

    val validator = JwtValidators.createDefault()

    decoder.setJwtValidator(validator)
    return decoder
}