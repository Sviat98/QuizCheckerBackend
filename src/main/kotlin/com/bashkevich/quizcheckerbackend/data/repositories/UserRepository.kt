package com.bashkevich.quizcheckerbackend.data.repositories

import com.bashkevich.quizcheckerbackend.data.models.user.CreateUserRequest
import com.bashkevich.quizcheckerbackend.data.models.user.UpdateUserRequest
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.jdbc.*

interface UserRepository {
    suspend fun getAllUsers(): List<User>
    suspend fun getUserById(id: Int): User?
    suspend fun createUser(request: CreateUserRequest): User
    suspend fun updateUser(id: Int, request: UpdateUserRequest): Boolean
    suspend fun deleteUser(id: Int): Boolean
}

