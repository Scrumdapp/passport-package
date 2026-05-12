package com.scrumdapp.passportstarter.filters

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

fun HttpSecurity.usePassport(
    filter: PassportAuthFilter
): HttpSecurity {
    return this
        .addFilterBefore(filter, BasicAuthenticationFilter::class.java)
        .sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
}