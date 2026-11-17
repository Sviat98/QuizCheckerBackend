package com.bashkevich.quizcheckerbackend.data.models.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String,
    @SerialName("email")
    val email: String
)

@Serializable
data class CreateUserRequest(
    @SerialName("name")
    val name: String,
    @SerialName("email")
    val email: String
)

/**
 * DTO for updating an existing user.
 */
@Serializable
data class UpdateUserRequest(
    @SerialName("name")
    val name: String? = null,
    @SerialName("email")
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
