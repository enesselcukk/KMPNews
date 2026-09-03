package com.example.kmpnews.feature.search.data.di

import com.example.kmpnews.feature.search.data.network.SearchApi
import com.example.kmpnews.feature.search.data.repository.SearchRepositoryImpl
import com.example.kmpnews.feature.search.domain.repository.SearchRepository
import org.koin.dsl.module

val searchDataModule = module {
    single { SearchApi(httpClient = get()) }
    single<SearchRepository> {
        SearchRepositoryImpl(searchApi = get())
    }
}
