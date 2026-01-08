package com.bashkevich.quizcheckerbackend.routes

import com.bashkevich.quizcheckerbackend.data.models.CreateUserRequest
import com.bashkevich.quizcheckerbackend.data.models.UpdateUserRequest
import com.bashkevich.quizcheckerbackend.services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userRoutes() {
    val userService by inject<UserService>()

    route("/api/users") {
        // GET /api/users - List all users
        get {
            val users = userService.getAllUsers()
            call.respond(HttpStatusCode.OK, users)
        }

        // GET /api/users/{id} - Get user by ID
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid user ID"))
                return@get
            }

            val user = userService.getUserById(id)
            if (user == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "User not found"))
            } else {
                call.respond(HttpStatusCode.OK, user)
            }
        }

        // POST /api/users - Create new user
        post {
            try {
                val request = call.receive<CreateUserRequest>()
                val user = userService.createUser(request)
                call.respond(HttpStatusCode.Created, user)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to create user"))
            }
        }

        // PUT /api/users/{id} - Update user
        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid user ID"))
                return@put
            }

            try {
                val request = call.receive<UpdateUserRequest>()
                val updated = userService.updateUser(id, request)
                if (updated) {
                    val user = userService.getUserById(id)
                    call.respond(HttpStatusCode.OK, user!!)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "User not found"))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Failed to update user"))
            }
        }

        // DELETE /api/users/{id} - Delete user
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid user ID"))
                return@delete
            }

            val deleted = userService.deleteUser(id)
            if (deleted) {
                call.respond(HttpStatusCode.OK, mapOf("message" to "User deleted successfully"))
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "User not found"))
            }
        }
    }
}
