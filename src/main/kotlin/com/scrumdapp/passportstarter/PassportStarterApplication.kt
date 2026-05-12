package com.scrumdapp.passportstarter

import com.scrumdapp.passportstarter.PassportProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(PassportProperties::class)
class PassportStarterApplication

fun main(args: Array<String>) {
    runApplication<PassportStarterApplication>(*args)
}
