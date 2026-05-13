package com.scrumdapp.passportplugin

import com.scrumdapp.passportplugin.annotations.PassportResolver
import com.scrumdapp.passportplugin.configs.PassportMvcConfig
import com.scrumdapp.passportplugin.filters.PassportAuthFilter
import com.scrumdapp.passportplugin.jwk.PassportService
import com.scrumdapp.passportplugin.jwk.jwtDecoder
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

    @Bean
    @ConditionalOnMissingBean
    fun passportResolver(passportService: PassportService): PassportResolver {
        return PassportResolver(passportService)
    }

    @Bean
    @ConditionalOnMissingBean
    fun passportMvcConfig(passportResolver: PassportResolver): PassportMvcConfig {
        return PassportMvcConfig(passportResolver)
    }
}