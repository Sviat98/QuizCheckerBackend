package com.bashkevich.quizcheckerbackend.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

/**
 * Configures HTTP-related features including CORS, default headers, and status pages.
 */
fun Application.configureHTTP() {
    // Configure CORS
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        anyHost() // For development - restrict this in production
    }

    // Configure default headers
    install(DefaultHeaders) {
        header("X-Engine", "Ktor")
    }

    // Configure status pages for error handling
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to (cause.message ?: "Unknown error"))
            )
        }

        status(HttpStatusCode.NotFound) { call, status ->
            call.respond(
                status,
                mapOf("error" to "Resource not found")
            )
        }
    }
}
