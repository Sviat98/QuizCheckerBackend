package com.bashkevich.quizcheckerbackend.data

import com.bashkevich.quizcheckerbackend.data.models.blanktemplate.AnswerTemplateTable
import com.bashkevich.quizcheckerbackend.data.models.blanktemplate.BlankTemplateTable
import com.bashkevich.quizcheckerbackend.data.models.blanktemplate.SlotAnswerTemplateTable
import com.bashkevich.quizcheckerbackend.data.models.blanktemplate.SlotTemplateTable
import com.bashkevich.quizcheckerbackend.data.models.user.UsersTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object DatabaseFactory {

    private const val DRIVER_CLASS_NAME = "org.postgresql.Driver"

    fun init() {
        val jdbcURL = System.getenv("DB_JDBC_URL")
            ?: throw IllegalStateException("DB_JDBC_URL environment variable is not set")
        val user = System.getenv("DB_USER")
            ?: throw IllegalStateException("DB_USER environment variable is not set")
        val password = System.getenv("DB_PASSWORD")
            ?: throw IllegalStateException("DB_PASSWORD environment variable is not set")

        val database = Database.connect(
            createHikariDataSource(
                url = jdbcURL,
                driver = DRIVER_CLASS_NAME,
                user = user,
                password = password
            )
        )

        // Create tables if they don't exist
        transaction(database) {
            SchemaUtils.create(
                UsersTable,
                BlankTemplateTable,
                AnswerTemplateTable,
                SlotTemplateTable,
                SlotAnswerTemplateTable
            )
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

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        withContext(Dispatchers.IO) {
            suspendTransaction { block() }
        }
}
