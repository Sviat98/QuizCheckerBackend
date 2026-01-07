package com.bashkevich.quizcheckerbackend.data.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

/**
 * Users table definition using Exposed ORM.
 */
object Users : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    val email = varchar("email", 255).uniqueIndex()

    override val primaryKey = PrimaryKey(id)
}

/**
 * Data class representing a User entity.
 * Used for JSON serialization/deserialization.
 */
@Serializable
data class User(
    val id: Int? = null,
    val name: String,
    val email: String
)

/**
 * DTO for creating a new user (without ID).
 */
@Serializable
data class CreateUserRequest(
    val name: String,
    val email: String
)

/**
 * DTO for updating an existing user.
 */
@Serializable
data class UpdateUserRequest(
    val name: String? = null,
    val email: String? = null
)
