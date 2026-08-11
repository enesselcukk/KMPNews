package com.example.kmpnews.feature.home.data.di

import com.example.kmpnews.feature.home.data.HomeNewsRepositoryImpl
import com.example.kmpnews.feature.home.data.network.HomeApi
import com.example.kmpnews.feature.home.domain.repository.HomeRepository
import org.koin.dsl.module

val homeDataModule = module {
    single { HomeApi(httpClient = get()) }
    single<HomeRepository> { HomeNewsRepositoryImpl(homeApi = get()) }
}
