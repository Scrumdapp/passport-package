package com.scrumdapp.passportplugin.filters

import com.scrumdapp.passportplugin.jwt.PassportService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.filter.OncePerRequestFilter

class PassportAuthFilter(
    private val passportService: PassportService
): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val jwt = passportService.decodeJwt(authHeader.substring("Bearer ".length))

        if (SecurityContextHolder.getContext().authentication == null) {

            val userId = passportService.extractUserId(jwt)
            val roles = passportService.extractRoles(jwt)

            val authToken = JwtAuthenticationToken(
                jwt,
                roles,
                userId.toString()
            )

            SecurityContextHolder.getContext().authentication = authToken
        }

        filterChain.doFilter(request, response)
    }
}