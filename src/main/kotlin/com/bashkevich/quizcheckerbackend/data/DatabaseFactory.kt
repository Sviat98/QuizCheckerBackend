package com.bashkevich.quizcheckerbackend.data

import com.bashkevich.quizcheckerbackend.data.models.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Database factory for initializing and managing database connections.
 * Uses HikariCP for connection pooling and Exposed for ORM.
 */
object DatabaseFactory {

    fun init(config: ApplicationConfig) {
        val driverClassName = config.property("storage.driverClassName").getString()
        val jdbcURL = config.property("storage.jdbcURL").getString()
        val user = config.property("storage.user").getString()
        val password = config.property("storage.password").getString()

        val database = Database.connect(
            createHikariDataSource(
                url = jdbcURL,
                driver = driverClassName,
                user = user,
                password = password
            )
        )

        // Create tables if they don't exist
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    private fun createHikariDataSource(
        url: String,
        driver: String,
        user: String,
        password: String
    ): HikariDataSource {
        val hikariConfig = HikariConfig().apply {
            driverClassName = driver
            jdbcUrl = url
            username = user
            this.password = password
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        return HikariDataSource(hikariConfig)
    }

    /**
     * Helper function to execute database queries in a suspended transaction.
     */
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
