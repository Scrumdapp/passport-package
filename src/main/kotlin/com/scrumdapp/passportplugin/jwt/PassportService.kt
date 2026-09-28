package com.scrumdapp.passportplugin.jwt

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtException
import java.util.Date

data class PassportContent(

    val userId: Long,
    val userGroups: List<Long>,
    val roles: List<String>,
)

class PassportService(
    private val jwtDecoder: JwtDecoder
) {

    fun decodeJwt(token: String): Jwt {
        try {
            val jwt = jwtDecoder.decode(token)
            if (jwt == null || isTokenExpired(jwt)) {
                throw JwtException("Token is expired")
            }
            return jwt
        } catch (ex: JwtException) {
            throw RuntimeException(ex)
        }
    }

    fun isTokenExpired(token: Jwt): Boolean {
        return token.expiresAt?.isBefore(Date().toInstant()) ?: throw RuntimeException("Invalid token. No expiry time was provided")
    }

    fun extractUserId(token: Jwt): Long {
        return token.subject.toLong()
    }

    fun extractUserGroups(token: Jwt): List<Long> {
        return token.getClaim("userGroups")
    }

    fun extractRoles(token: Jwt): List<GrantedAuthority> {
        val roles = token.getClaim<List<String>>("roles")
        return roles.map { SimpleGrantedAuthority(it) }
    }

    fun extractPassport(token: Jwt): PassportContent {
        return PassportContent(
            token.subject.toLong(),
            token.getClaim("userGroups") ?: emptyList(),
            token.getClaim<List<String>>("roles") ?: emptyList(),
        )
    }

    fun getPassport(): PassportContent {
        val jwt = SecurityContextHolder.getContext().authentication?.principal as? Jwt
            ?: throw IllegalStateException("Auth principal couldn't be found or isn't a valid jwt.")
        return extractPassport(jwt)
    }
}