package com.example.kmpnews.feature.detail.presentation.di

import com.example.kmpnews.core.navigation.NavEntryProvider
import com.example.kmpnews.feature.detail.presentation.DetailViewModel
import com.example.kmpnews.feature.detail.presentation.navigate.DetailProvider
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val detailPresentationModule = module {
    single<NavEntryProvider>(named("DetailProvider")) {
        DetailProvider()
    }

    viewModel { parameters ->
        DetailViewModel(
            articleUrl = parameters.get(),
            getArticleDetailUseCase = get(),
            navigationManager = get(),
        )
    }
}
