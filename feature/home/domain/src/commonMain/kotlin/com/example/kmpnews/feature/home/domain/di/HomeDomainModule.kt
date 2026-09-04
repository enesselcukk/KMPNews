package com.example.kmpnews.feature.home.domain.di

import com.example.kmpnews.feature.home.domain.usecase.CacheHomeArticlesUseCase
import com.example.kmpnews.feature.home.domain.usecase.HomeNewsUseCase
import org.koin.dsl.module

val homeDomainModule = module {
    factory { HomeNewsUseCase(homeRepository = get()) }
    factory { CacheHomeArticlesUseCase(homeRepository = get()) }
}