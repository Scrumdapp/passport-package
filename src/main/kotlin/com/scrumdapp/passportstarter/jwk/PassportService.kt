package com.scrumdapp.passportstarter.jwk

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtException
import java.util.Date

data class PassportContent(

    val userId: Int?,
    val userGroups: List<Int>,
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

    fun extractUserId(token: Jwt): Int {
        return token.subject.toInt()
    }

    fun extractRoles(token: Jwt): List<GrantedAuthority> {
        val roles = token.getClaim<List<String>>("roles")
        return roles.map { SimpleGrantedAuthority(it) }
    }

    fun extractPassport(token: Jwt): PassportContent {
        return PassportContent(
            token.subject.toInt(),
            token.getClaim<List<Long>>("userGroups").map { it.toInt() },
            token.getClaim<List<String>>("roles") ?: emptyList(),
        )
    }
}