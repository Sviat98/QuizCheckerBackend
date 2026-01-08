package com.bashkevich.quizcheckerbackend.data.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int? = null,
    val name: String,
    val email: String
)

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

/**
 * Extension function to convert UserEntity to UserDto
 */
fun UserEntity.toDto(): UserDto = UserDto(
    id = this.id.value,
    name = this.name,
    email = this.email
)
