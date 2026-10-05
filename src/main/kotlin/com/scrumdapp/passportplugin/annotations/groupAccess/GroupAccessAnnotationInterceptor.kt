package com.scrumdapp.passportplugin.annotations.groupAccess

import com.scrumdapp.passportplugin.jwt.PassportService
import com.scrumdapp.passportplugin.utils.getTemplateVariables
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.HandlerMapping

@Component
class GroupAccessAnnotationInterceptor(
    private val passportService: PassportService
): HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

        if (handler is HandlerMethod) {
            val annotation = handler.getMethodAnnotation(IsInGroup::class.java)

            if (annotation != null) {
                var pName = "groupId"
                val v = annotation.paramName
                if (!v.isEmpty()) {
                    pName = v
                }

                val pathVariables = request.getTemplateVariables()

                val groupId = pathVariables[pName]?.toLong()
                    ?: throw IllegalStateException("Could not find parameter $pName in uri ${request.requestURI}")

                val userGroups = getUserGroups()

                if (!userGroups.contains(groupId)) {
                    throw AccessDeniedException("Access to group denied")
                }
            }
        }
        return true
    }

    private fun getUserGroups(): List<Long> {
        val jwt = SecurityContextHolder.getContext().authentication?.principal as? Jwt
            ?: throw IllegalStateException("Auth principal couldn't be found or isn't a valid jwt.")
        return passportService.extractUserGroups(jwt)
    }
}
