package com.bashkevich.quizcheckerbackend.data.repositories

import com.bashkevich.quizcheckerbackend.data.DatabaseFactory.dbQuery
import com.bashkevich.quizcheckerbackend.data.models.CreateUserRequest
import com.bashkevich.quizcheckerbackend.data.models.UpdateUserRequest
import com.bashkevich.quizcheckerbackend.data.models.User
import com.bashkevich.quizcheckerbackend.data.models.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

/**
 * Repository interface for User entity operations.
 */
interface UserRepository {
    suspend fun getAllUsers(): List<User>
    suspend fun getUserById(id: Int): User?
    suspend fun createUser(request: CreateUserRequest): User
    suspend fun updateUser(id: Int, request: UpdateUserRequest): Boolean
    suspend fun deleteUser(id: Int): Boolean
}

/**
 * Implementation of UserRepository using Exposed ORM.
 * Provides CRUD operations for User entities.
 */
class UserRepositoryImpl : UserRepository {

    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        name = row[Users.name],
        email = row[Users.email]
    )

    override suspend fun getAllUsers(): List<User> = dbQuery {
        Users.selectAll().map(::resultRowToUser)
    }

    override suspend fun getUserById(id: Int): User? = dbQuery {
        Users
            .selectAll()
            .where { Users.id eq id }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun createUser(request: CreateUserRequest): User = dbQuery {
        val insertStatement = Users.insert {
            it[name] = request.name
            it[email] = request.email
        }
        val insertedId = insertStatement[Users.id]
        User(
            id = insertedId,
            name = request.name,
            email = request.email
        )
    }

    override suspend fun updateUser(id: Int, request: UpdateUserRequest): Boolean = dbQuery {
        val updated = Users.update({ Users.id eq id }) {
            request.name?.let { name -> it[Users.name] = name }
            request.email?.let { email -> it[Users.email] = email }
        }
        updated > 0
    }

    override suspend fun deleteUser(id: Int): Boolean = dbQuery {
        Users.deleteWhere { Users.id eq id } > 0
    }
}
