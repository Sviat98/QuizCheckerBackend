package com.bashkevich.quizcheckerbackend.plugins

import com.bashkevich.quizcheckerbackend.di.appModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

/**
 * Configures Koin dependency injection for the application.
 * This sets up the DI container and registers all application modules.
 */
fun Application.configureDependencyInjection() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
}
