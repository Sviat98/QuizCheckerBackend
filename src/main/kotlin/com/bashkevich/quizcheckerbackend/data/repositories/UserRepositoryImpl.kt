package com.bashkevich.quizcheckerbackend.data.repositories

import com.bashkevich.quizcheckerbackend.data.models.user.CreateUserRequest
import com.bashkevich.quizcheckerbackend.data.models.user.UpdateUserRequest
import com.bashkevich.quizcheckerbackend.data.models.user.UserEntity

class UserRepositoryImpl : UserRepository {

    override suspend fun getAllUsers(): List<UserEntity> {
        return UserEntity.all().toList()
    }

    override suspend fun getUserById(id: Int): UserEntity? {
        return UserEntity.findById(id)
    }

    override suspend fun createUser(request: CreateUserRequest): UserEntity {
        return UserEntity.new {
            name = request.name
            email = request.email
        }
    }

    override suspend fun updateUser(id: Int, request: UpdateUserRequest): Boolean {
        val user = UserEntity.findById(id) ?: return false
        request.name?.let { user.name = it }
        request.email?.let { user.email = it }
        return true
    }

    override suspend fun deleteUser(id: Int): Boolean {
        val user = UserEntity.findById(id) ?: return false
        user.delete()
        return true
    }
}
