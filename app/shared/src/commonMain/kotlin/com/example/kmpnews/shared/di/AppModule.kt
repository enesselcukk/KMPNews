package com.example.kmpnews.shared.di

import com.example.kmpnews.core.datastore.UserSettingsRepositoryImpl
import com.example.kmpnews.core.datastore.UserSettingsRepository
import org.koin.dsl.module

val appModule = module {
    single<UserSettingsRepository> { UserSettingsRepositoryImpl() }
}
