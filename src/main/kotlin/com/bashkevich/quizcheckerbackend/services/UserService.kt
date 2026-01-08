package com.bashkevich.quizcheckerbackend.services

import com.bashkevich.quizcheckerbackend.data.models.CreateUserRequest
import com.bashkevich.quizcheckerbackend.data.models.UpdateUserRequest
import com.bashkevich.quizcheckerbackend.data.models.User
import com.bashkevich.quizcheckerbackend.data.repositories.UserRepository

class UserService(private val userRepository: UserRepository) {

    suspend fun getAllUsers(): List<User> {
        return userRepository.getAllUsers()
    }

    suspend fun getUserById(id: Int): User? {
        return userRepository.getUserById(id)
    }

    suspend fun createUser(request: CreateUserRequest): User {
        // Add business logic here (e.g., validation, email verification)
        validateUserRequest(request)
        return userRepository.createUser(request)
    }

    suspend fun updateUser(id: Int, request: UpdateUserRequest): Boolean {
        // Add business logic here
        return userRepository.updateUser(id, request)
    }

    suspend fun deleteUser(id: Int): Boolean {
        return userRepository.deleteUser(id)
    }

    private fun validateUserRequest(request: CreateUserRequest) {
        require(request.name.isNotBlank()) { "Name cannot be blank" }
        require(request.email.isNotBlank()) { "Email cannot be blank" }
        require(request.email.contains("@")) { "Invalid email format" }
    }
}
