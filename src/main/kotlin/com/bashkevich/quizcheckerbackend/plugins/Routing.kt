package com.bashkevich.quizcheckerbackend.plugins

import com.bashkevich.quizcheckerbackend.routes.blankTemplateRoutes
import com.bashkevich.quizcheckerbackend.routes.checkAnswerRoutes
import com.bashkevich.quizcheckerbackend.routes.healthRoutes
import com.bashkevich.quizcheckerbackend.routes.koogRoutes
import com.bashkevich.quizcheckerbackend.routes.userRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        healthRoutes()
        userRoutes()
        koogRoutes()
        blankTemplateRoutes()
        checkAnswerRoutes()
    }
}
