package com.example.kmpnews.feature.home.data.di

import com.example.kmpnews.feature.home.data.local.HomeNewsLocalDataSource
import com.example.kmpnews.feature.home.data.network.HomeApi
import com.example.kmpnews.feature.home.data.repository.HomeNewsRepositoryImpl
import com.example.kmpnews.feature.home.domain.repository.HomeRepository
import org.koin.dsl.module

val homeDataModule = module {
    single { HomeApi(httpClient = get()) }
    single { HomeNewsLocalDataSource(homeHeadlineDao = get()) }
    single<HomeRepository> {
        HomeNewsRepositoryImpl(
            homeApi = get(),
            localDataSource = get(),
        )
    }
}
