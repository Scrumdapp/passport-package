package com.scrumdapp.passportstarter.configs

import com.scrumdapp.passportstarter.filters.PassportAuthFilter
import com.scrumdapp.passportstarter.jwk.PassportService
import com.scrumdapp.passportstarter.jwk.jwtDecoder
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.web.SecurityFilterChain

@Configuration
@ConditionalOnClass(SecurityFilterChain::class)
@ConditionalOnProperty(
    prefix = "passport",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class PassportAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun passportService(properties: PassportProperties): PassportService {
        return PassportService(jwtDecoder(properties))
    }

    @Bean
    @ConditionalOnMissingBean
    fun passportFilter(service: PassportService): PassportAuthFilter {
        return PassportAuthFilter(service)
    }
}