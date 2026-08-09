package com.example.kmpnews.shared.di

import com.example.kmpnews.core.datastore.DefaultUserSettingsRepository
import com.example.kmpnews.core.datastore.UserSettingsRepository
import com.example.kmpnews.core.domain.repository.ArticleRepository
import com.example.kmpnews.feature.detail.domain.GetArticleDetailUseCase
import com.example.kmpnews.feature.detail.presentation.DetailViewModel
import com.example.kmpnews.feature.home.data.HomeArticleRepository
import com.example.kmpnews.feature.home.domain.repository.HomeRepository
import com.example.kmpnews.feature.home.domain.usecase.GetHomeArticlesUseCase
import com.example.kmpnews.feature.home.presentation.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<UserSettingsRepository> { DefaultUserSettingsRepository() }

    factory { GetHomeArticlesUseCase(get()) }
    factory { GetArticleDetailUseCase(get()) }

    viewModel { HomeViewModel(get()) }
    viewModel { parameters ->
        DetailViewModel(
            articleId = parameters.get(),
            getArticleDetailUseCase = get(),
        )
    }
}
