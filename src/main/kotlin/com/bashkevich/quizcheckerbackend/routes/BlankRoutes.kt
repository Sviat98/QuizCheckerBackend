package com.bashkevich.quizcheckerbackend.routes

import com.bashkevich.quizcheckerbackend.services.blank.BlankService
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.blankRoutes() {
    val blankService by inject<BlankService>()

    route("/api/quiz") {
        // POST /api/quiz/start - Start a quiz by creating blanks from all templates
        post("/start") {
            val blanks = blankService.startQuiz()
            call.respond(HttpStatusCode.Created, blanks)
        }
    }

    route("/api/blanks") {
        // GET /api/blanks - Get all blanks
        get {
            val blanks = blankService.getAllBlanks()
            call.respond(HttpStatusCode.OK, blanks)
        }

        // GET /api/blanks/{id} - Get blank by ID
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: 0
            val blank = blankService.getBlankById(id)
            call.respond(HttpStatusCode.OK, blank)
        }
    }
}
