package com.example.kmpnews.shared.di

import com.example.kmpnews.core.database.DatabaseFactory
import com.example.kmpnews.core.database.NewsDatabase
import org.koin.dsl.module

val jvmPlatformModule = module {
    single<NewsDatabase> {
        DatabaseFactory().createDatabase()
    }
}

fun initAppKoin() {
    startKoinWithModules(listOf(jvmPlatformModule))
}
