package com.example.kmpnews.shared.di

import com.example.kmpnews.core.datastore.DefaultUserSettingsRepository
import com.example.kmpnews.core.datastore.UserSettingsRepository
import org.koin.dsl.module

val appModule = module {
    single<UserSettingsRepository> { DefaultUserSettingsRepository() }
}
