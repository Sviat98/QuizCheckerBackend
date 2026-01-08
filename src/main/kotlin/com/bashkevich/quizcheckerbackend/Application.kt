package com.bashkevich.quizcheckerbackend

import com.bashkevich.quizcheckerbackend.data.DatabaseFactory
import com.bashkevich.quizcheckerbackend.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    // Initialize database
    DatabaseFactory.init()

    // Configure plugins
    configureDependencyInjection()
    configureSerialization()
    configureHTTP()
    configureMonitoring()
    configureRouting()
}
