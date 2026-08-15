package com.example.kmpnews.feature.detail.domain.di

import com.example.kmpnews.feature.detail.domain.GetArticleDetailUseCase
import org.koin.dsl.module

val detailDomainModule = module {
    factory { GetArticleDetailUseCase(detailRepository = get()) }
}
