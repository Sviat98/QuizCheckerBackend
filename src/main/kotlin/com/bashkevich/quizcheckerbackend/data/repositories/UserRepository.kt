package com.bashkevich.quizcheckerbackend.data.repositories

import com.bashkevich.quizcheckerbackend.data.models.user.CreateUserRequest
import com.bashkevich.quizcheckerbackend.data.models.user.UpdateUserRequest
import com.bashkevich.quizcheckerbackend.data.models.user.UserEntity
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.jdbc.*

interface UserRepository {
    suspend fun getAllUsers(): List<UserEntity>
    suspend fun getUserById(id: Int): UserEntity?
    suspend fun createUser(request: CreateUserRequest): UserEntity
    suspend fun updateUser(id: Int, request: UpdateUserRequest): Boolean
    suspend fun deleteUser(id: Int): Boolean
}

