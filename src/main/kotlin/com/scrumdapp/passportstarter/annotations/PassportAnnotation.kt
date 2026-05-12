package com.scrumdapp.passportstarter.annotations

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Passport(
    val filter: String = ""
)

