package com.example.kmpnews.shared.di

import com.example.kmpnews.core.database.DatabaseFactory
import com.example.kmpnews.core.database.NewsDatabase
import org.koin.dsl.module

val wasmJsPlatformModule = module {
    single<NewsDatabase> {
        DatabaseFactory().createDatabase()
    }
    single { get<NewsDatabase>().homeHeadlineDao() }
}

fun initAppKoin() {
    startKoinWithModules(listOf(wasmJsPlatformModule))
}
