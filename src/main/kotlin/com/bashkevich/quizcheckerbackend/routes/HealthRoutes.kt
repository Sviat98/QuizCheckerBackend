package com.bashkevich.quizcheckerbackend.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable


fun Route.healthRoutes() {
    // Health check endpoint
    get("/health") {
        call.respond(
            status = HttpStatusCode.OK,
            message = "QuizChecker Backend is running"
        )
    }

    // API routes
    route("/api") {
        // Simple hello endpoint
        get("/hello") {
            call.respond(
                status = HttpStatusCode.OK,
                message = "Hello from QuizChecker Backend!",
            )
        }
    }
}
