package com.bashkevich.quizcheckerbackend.di

import com.bashkevich.quizcheckerbackend.data.repositories.UserRepository
import com.bashkevich.quizcheckerbackend.data.repositories.UserRepositoryImpl
import com.bashkevich.quizcheckerbackend.data.repositories.blanktemplate.BlankTemplateRepository
import com.bashkevich.quizcheckerbackend.data.repositories.blanktemplate.BlankTemplateRepositoryImpl
import com.bashkevich.quizcheckerbackend.services.UserService
import com.bashkevich.quizcheckerbackend.services.blanktemplate.BlankTemplateService
import org.koin.dsl.module

val appModule = module {
    // Repositories
    single<UserRepository> { UserRepositoryImpl() }
    single<BlankTemplateRepository> { BlankTemplateRepositoryImpl() }

    // Services
    single { UserService(get()) }
    single { BlankTemplateService(get()) }
}
