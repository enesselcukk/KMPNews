package com.example.kmpnews.core.navigation

import org.koin.dsl.module

val navigationModule = module {
    single<NavigationManager> { NavigationManagerImpl() }
}
