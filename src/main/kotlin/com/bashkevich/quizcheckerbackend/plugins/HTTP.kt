package com.bashkevich.quizcheckerbackend.plugins

import com.bashkevich.quizcheckerbackend.data.models.message.ResponseMessageDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.jetbrains.exposed.v1.dao.exceptions.EntityNotFoundException

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
        exception<InvalidBodyException> { call, cause ->
            call.respondWithMessageBody(statusCode = HttpStatusCode.BadRequest, message = cause.message ?: "")
        }
        exception<UnauthorizedException> { call, cause ->
            call.respondWithMessageBody(statusCode = HttpStatusCode.Unauthorized, message = cause.message ?: "")
        }
        exception<BadRequestException> { call, cause ->
            call.respondWithMessageBody(statusCode = HttpStatusCode.BadRequest, message = cause.message ?: "")
        }
        exception<NotFoundException> { call, cause ->
            call.respondWithMessageBody(statusCode = HttpStatusCode.NotFound, message = cause.message ?: "")
        }
        exception<EntityNotFoundException>{ call, cause ->
            val entityId = cause.id
            val entityClass = cause.entity.javaClass

            call.respondWithMessageBody(statusCode = HttpStatusCode.NotFound, "Entity $entityClass with id = $entityId not found")
        }
        exception<Throwable> { call, cause ->
            call.respondWithMessageBody(statusCode = HttpStatusCode.InternalServerError, message = cause.message ?: "")
        }
    }
}

class UnauthorizedException(message: String = "Token is not valid or has expired!") : Exception(message)

class InvalidBodyException(message: String = "Invalid body format in request!") : Exception(message)

suspend inline fun ApplicationCall.respondWithMessageBody(
    statusCode: HttpStatusCode = HttpStatusCode.OK,
    message: String,
) {
    respond(statusCode, ResponseMessageDto(message))
}
