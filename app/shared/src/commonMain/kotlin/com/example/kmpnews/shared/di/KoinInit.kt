package com.example.kmpnews.shared.di

import com.example.kmpnews.core.network.di.newsApiModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

fun startKoinWithModules(
    platformModules: List<Module>,
    appDeclaration: KoinApplication.() -> Unit = {},
) {
    startKoin {
        appDeclaration()
        modules(appModule + newsApiModule + platformModules)
    }
}
