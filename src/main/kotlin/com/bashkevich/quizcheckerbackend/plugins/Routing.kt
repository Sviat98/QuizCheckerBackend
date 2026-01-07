package com.bashkevich.quizcheckerbackend.plugins

import com.bashkevich.quizcheckerbackend.routes.healthRoutes
import com.bashkevich.quizcheckerbackend.routes.userRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

/**
 * Configures application routing.
 * All route definitions are organized in separate route files.
 */
fun Application.configureRouting() {
    routing {
        healthRoutes()
        userRoutes()
    }
}
