 package com.bashkevich.quizcheckerbackend.di

import com.bashkevich.quizcheckerbackend.data.repositories.UserRepository
import com.bashkevich.quizcheckerbackend.data.repositories.UserRepositoryImpl
import com.bashkevich.quizcheckerbackend.services.UserService
import org.koin.dsl.module

/**
 * Koin dependency injection module.
 * Defines all dependencies and their relationships.
 */
val appModule = module {
    // Repositories
    single<UserRepository> { UserRepositoryImpl() }

    // Services
    single { UserService(get()) }
}
