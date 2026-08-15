package com.example.kmpnews.shared.di

import com.example.kmpnews.core.navigation.navigationModule
import com.example.kmpnews.core.network.di.newsApiModule
import com.example.kmpnews.feature.detail.presentation.di.detailPresentationModule
import com.example.kmpnews.feature.home.data.di.homeDataModule
import com.example.kmpnews.feature.home.domain.di.homeDomainModule
import com.example.kmpnews.feature.home.presentation.di.homePresentationModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

private val sharedModules = listOf(
    appModule,
    newsApiModule,
    navigationModule,
    homeDataModule,
    homeDomainModule,
    homePresentationModule,
    detailPresentationModule,
)

fun startKoinWithModules(
    platformModules: List<Module> = emptyList(),
    appDeclaration: KoinApplication.() -> Unit = {},
) {
    startKoin {
        appDeclaration()
        modules(sharedModules + platformModules)
    }
}
