package com.example.kmpnews.shared.di

import com.example.kmpnews.core.database.DatabaseFactory
import com.example.kmpnews.core.database.NewsDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidPlatformModule = module {
    single<NewsDatabase> {
        DatabaseFactory(get()).createDatabase()
    }
}

fun initAppKoin(context: android.content.Context) {
    startKoinWithModules(listOf(androidPlatformModule)) {
        androidContext(context.applicationContext)
    }
}
