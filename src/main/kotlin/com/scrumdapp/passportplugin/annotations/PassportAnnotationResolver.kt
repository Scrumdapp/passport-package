package com.scrumdapp.passportplugin.annotations

import com.scrumdapp.passportplugin.jwt.PassportService
import lombok.AllArgsConstructor
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@AllArgsConstructor
class PassportResolver(
    private val passportService: PassportService,
): HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(Passport::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        val jwt = SecurityContextHolder.getContext().authentication?.principal as? Jwt
            ?: throw IllegalStateException("Auth principal couldn't be found or isn't a valid jwt. To prevent the endpoint is protected.")
        return passportService.extractPassport(jwt)
    }

}