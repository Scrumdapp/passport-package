package com.scrumdapp.passportplugin.configs

import com.scrumdapp.passportplugin.annotations.PassportResolver
import lombok.AllArgsConstructor
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@AllArgsConstructor
class PassportMvcConfig(
    private val passportResolver: PassportResolver
): WebMvcConfigurer {

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(passportResolver)
    }
}