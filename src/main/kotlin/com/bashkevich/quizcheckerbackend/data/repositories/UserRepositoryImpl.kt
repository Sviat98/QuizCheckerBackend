package com.bashkevich.quizcheckerbackend.data.repositories

import com.bashkevich.quizcheckerbackend.data.DatabaseFactory.dbQuery
import com.bashkevich.quizcheckerbackend.data.models.user.CreateUserRequest
import com.bashkevich.quizcheckerbackend.data.models.user.UpdateUserRequest
import com.bashkevich.quizcheckerbackend.data.models.user.UserEntity

class UserRepositoryImpl : UserRepository {

    override suspend fun getAllUsers(): List<UserEntity> = dbQuery {
        UserEntity.all().toList()
    }

    override suspend fun getUserById(id: Int): UserEntity? = dbQuery {
        UserEntity.findById(id)
    }

    override suspend fun createUser(request: CreateUserRequest): UserEntity = dbQuery {
        UserEntity.new {
            name = request.name
            email = request.email
        }
    }

    override suspend fun updateUser(id: Int, request: UpdateUserRequest): Boolean = dbQuery {
        val user = UserEntity.findById(id) ?: return@dbQuery false
        request.name?.let { user.name = it }
        request.email?.let { user.email = it }
        true
    }

    override suspend fun deleteUser(id: Int): Boolean = dbQuery {
        val user = UserEntity.findById(id) ?: return@dbQuery false
        user.delete()
        true
    }
}
