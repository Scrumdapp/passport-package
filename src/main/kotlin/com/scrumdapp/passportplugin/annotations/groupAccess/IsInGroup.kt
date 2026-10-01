package com.scrumdapp.passportplugin.annotations.groupAccess

import com.scrumdapp.passportplugin.jwt.PassportService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.HandlerMapping

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class IsInGroup(
    val paramName: String = ""
)

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

                val pathVariables =
                    request.getAttribute(
                        HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE
                    ) as MutableMap<String, String>

                val groupId = pathVariables[pName]?.toLong()
                    ?: throw IllegalStateException("Could not find paramater $pName in uri ${request.requestURI}")

                val userGroups = getUserGroups()

                if (!userGroups.contains(groupId)) {
                    // Change this with the correct error
                    throw Exception("No access to group")
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