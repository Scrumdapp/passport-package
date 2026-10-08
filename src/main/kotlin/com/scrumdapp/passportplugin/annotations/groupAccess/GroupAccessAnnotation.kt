package com.scrumdapp.passportplugin.annotations.groupAccess

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class IsInGroup(
    val paramName: String = ""
)

