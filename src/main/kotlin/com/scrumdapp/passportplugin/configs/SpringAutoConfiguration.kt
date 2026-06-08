package com.scrumdapp.passportplugin.configs

import com.scrumdapp.passportplugin.PassportProperties
import com.scrumdapp.passportplugin.filters.PassportAuthFilter
import com.scrumdapp.passportplugin.jwt.PassportService
import com.scrumdapp.passportplugin.jwt.jwtDecoder
import com.scrumdapp.passportplugin.utils.PassportUtilService
import com.scrumdapp.passportplugin.utils.annotations.PassportResolver
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(PassportProperties::class)
class SpringAutoConfiguration {

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
    fun passportUtilService(service: PassportService): PassportUtilService {
        return PassportUtilService(service)
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