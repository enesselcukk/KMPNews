package com.example.kmpnews.feature.home.presentation.di

import com.example.kmpnews.core.navigation.NavEntryProvider
import com.example.kmpnews.feature.home.presentation.navigate.HomeProvider
import com.example.kmpnews.feature.home.presentation.ui.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val homePresentationModule = module {
    single<NavEntryProvider>(named("HomeProvider")) {
        HomeProvider()
    }

    viewModelOf(::HomeViewModel)
}
