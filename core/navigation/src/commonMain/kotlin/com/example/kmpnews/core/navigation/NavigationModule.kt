package com.example.kmpnews.core.navigation

import com.spaceex.core.navigation.NavigationManager
import com.spaceex.core.navigation.NavigationManagerImpl
import org.koin.dsl.module

val navigationModule = module {
    single<NavigationManager> { NavigationManagerImpl() }
}
