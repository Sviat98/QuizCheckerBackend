package com.bashkevich.quizcheckerbackend.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.CallLogging
import org.slf4j.event.Level

fun Application.configureMonitoring() {
    install(CallLogging){
        level = Level.INFO
    }
}
