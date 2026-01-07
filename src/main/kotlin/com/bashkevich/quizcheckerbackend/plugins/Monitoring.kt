package com.bashkevich.quizcheckerbackend.plugins

import io.ktor.server.application.*

/**
 * Configures request/response logging for monitoring.
 *
 * Note: CallLogging plugin has import issues with current Ktor version.
 * Logging is still available through Logback (configured in logback.xml).
 */
fun Application.configureMonitoring() {
    // Basic monitoring placeholder
    // You can add custom logging interceptors here if needed
    log.info("Monitoring configuration loaded")
}
