package com.example.kmpnews.iosApp.di

import com.example.kmpnews.core.database.DatabaseFactory
import com.example.kmpnews.core.database.NewsDatabase
import com.example.kmpnews.shared.di.startKoinWithModules
import org.koin.dsl.module

private val iosPlatformModule = module {
    single<NewsDatabase> {
        DatabaseFactory().createDatabase()
    }
}

fun initAppKoin() {
    startKoinWithModules(listOf(iosPlatformModule))
}
