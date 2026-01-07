package com.bashkevich.quizcheckerbackend.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class HealthResponse(
    val status: String,
    val message: String
)

@Serializable
data class HelloResponse(
    val message: String,
    val timestamp: Long
)

@Serializable
data class EchoRequest(
    val message: String
)

@Serializable
data class EchoResponse(
    val echo: String
)

/**
 * Health check and basic API routes.
 */
fun Route.healthRoutes() {
    // Health check endpoint
    get("/health") {
        call.respond(
            HttpStatusCode.OK,
            HealthResponse(
                status = "UP",
                message = "QuizChecker Backend is running"
            )
        )
    }

    // API routes
    route("/api") {
        // Simple hello endpoint
        get("/hello") {
            call.respond(
                HttpStatusCode.OK,
                HelloResponse(
                    message = "Hello from QuizChecker Backend!",
                    timestamp = System.currentTimeMillis()
                )
            )
        }

        // Echo endpoint
        post("/echo") {
            val request = call.receive<EchoRequest>()
            call.respond(
                HttpStatusCode.OK,
                EchoResponse(echo = request.message)
            )
        }
    }
}
