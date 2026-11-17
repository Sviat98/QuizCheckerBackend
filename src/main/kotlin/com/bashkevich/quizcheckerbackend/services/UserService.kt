package com.bashkevich.quizcheckerbackend.services

import com.bashkevich.quizcheckerbackend.data.DatabaseFactory.dbQuery
import com.bashkevich.quizcheckerbackend.data.models.user.CreateUserRequest
import com.bashkevich.quizcheckerbackend.data.models.user.UpdateUserRequest
import com.bashkevich.quizcheckerbackend.data.models.user.UserDto
import com.bashkevich.quizcheckerbackend.data.models.user.toDto
import com.bashkevich.quizcheckerbackend.data.repositories.UserRepository
import io.ktor.server.plugins.*

class UserService(private val userRepository: UserRepository) {

    suspend fun getAllUsers(): List<UserDto> = dbQuery {
        userRepository.getAllUsers().map { it.toDto() }
    }

    suspend fun getUserById(id: Int): UserDto? = dbQuery {
        if (id == 0) throw BadRequestException("Invalid user ID")
        userRepository.getUserById(id)?.toDto()
    }

    suspend fun createUser(request: CreateUserRequest): UserDto = dbQuery {
        // Add business logic here (e.g., validation, email verification)
        validateUserRequest(request)
        userRepository.createUser(request).toDto()
    }

    suspend fun updateUser(id: Int, request: UpdateUserRequest): Boolean = dbQuery {
        if (id == 0) throw BadRequestException("Invalid user ID")
        // Add business logic here
        userRepository.updateUser(id, request)
    }

    suspend fun deleteUser(id: Int): Boolean = dbQuery {
        if (id == 0) throw BadRequestException("Invalid user ID")
        userRepository.deleteUser(id)
    }

    private fun validateUserRequest(request: CreateUserRequest) {
        require(request.name.isNotBlank()) { "Name cannot be blank" }
        require(request.email.isNotBlank()) { "Email cannot be blank" }
        require(request.email.contains("@")) { "Invalid email format" }
    }
}
