package com.scrumdapp.passportplugin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(PassportProperties::class)
class PassportPluginApplication

fun main(args: Array<String>) {
    runApplication<PassportPluginApplication>(*args)
}
