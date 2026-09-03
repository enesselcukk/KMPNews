package com.example.kmpnews.feature.search.domain.di

import com.example.kmpnews.feature.search.domain.usecase.SearchArticlesUseCase
import org.koin.dsl.module

val searchDomainModule = module {
    factory { SearchArticlesUseCase(searchRepository = get()) }
}
