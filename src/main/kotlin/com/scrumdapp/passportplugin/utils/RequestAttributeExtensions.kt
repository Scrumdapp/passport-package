package com.scrumdapp.passportplugin.utils

import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.servlet.HandlerMapping

fun HttpServletRequest.getTemplateVariables(): Map<String, String> =
    getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)
        .let { it as Map<*, *> }
        .mapNotNull { (key, value) ->
            if (key is String && value is String) key to value else null
        }
        .toMap()