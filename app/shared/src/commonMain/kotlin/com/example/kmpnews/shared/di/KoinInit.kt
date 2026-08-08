package com.example.kmpnews.shared.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

fun startKoinWithModules(
    platformModules: List<Module>,
    appDeclaration: KoinApplication.() -> Unit = {},
) {
    startKoin {
        appDeclaration()
        modules(appModule + platformModules)
    }
}
