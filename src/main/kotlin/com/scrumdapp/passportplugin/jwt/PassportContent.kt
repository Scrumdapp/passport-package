package com.scrumdapp.passportplugin.jwt

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt

class PassportContent(
    val userId: Long,
    val userGroups: List<Long>,
    val roles: List<String>,
) {
    companion object {
        fun fromStaticSecurityContext(): PassportContent {
            val jwt = SecurityContextHolder.getContext().authentication?.principal as? Jwt
                ?: throw IllegalStateException("Auth principal couldn't be found or isn't a valid jwt.")

            return PassportContent(
                jwt.subject.toLong(),
                jwt.getClaim("userGroups") ?: emptyList(),
                jwt.getClaim<List<String>>("roles") ?: emptyList(),
            )
        }
    }
}