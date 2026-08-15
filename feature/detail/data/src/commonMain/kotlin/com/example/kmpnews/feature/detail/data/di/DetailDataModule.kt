package com.example.kmpnews.feature.detail.data.di

import com.example.kmpnews.feature.detail.data.repository.DetailRepositoryImpl
import com.example.kmpnews.feature.detail.domain.repository.DetailRepository
import org.koin.dsl.module

val detailDataModule = module {
    single<DetailRepository> { DetailRepositoryImpl(homeRepository = get()) }
}
