package com.scrumdapp.passportplugin.annotations.groupAccess

import com.scrumdapp.passportplugin.jwt.PassportContent
import com.scrumdapp.passportplugin.jwt.PassportService
import com.scrumdapp.passportplugin.utils.getTemplateVariables
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class GroupAccessAnnotationInterceptor(
): HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

        if (handler !is HandlerMethod) return true

        val paramName = handler.getMethodAnnotation(IsInGroup::class.java)?.paramName ?: "groupId"
        val pathVariables = request.getTemplateVariables()

        val param = pathVariables[paramName]
            ?: throw IllegalStateException("Could not find parameter $paramName in uri ${request.requestURI}")

        val groupId = param.toLongOrNull() ?: throw IllegalStateException("Couldn't parse $param to Long")

        val userGroups = PassportContent.fromStaticSecurityContext().userGroups

        if (!userGroups.contains(groupId)) {
            throw AccessDeniedException("Access to group denied")
        }
        return true
    }
}
