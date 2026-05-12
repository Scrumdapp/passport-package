package com.scrumdapp.passportstarter

import com.scrumdapp.passportstarter.filters.PassportAuthFilter
import com.scrumdapp.passportstarter.jwk.PassportService
import com.scrumdapp.passportstarter.jwk.jwtDecoder
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(PassportProperties::class)
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