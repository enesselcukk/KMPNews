package com.example.kmpnews.shared.di

import com.example.kmpnews.core.datastore.DefaultUserSettingsRepository
import com.example.kmpnews.core.datastore.UserSettingsRepository
import com.example.kmpnews.core.model.ArticleRepository
import com.example.kmpnews.core.network.createDefaultHttpClient
import com.example.kmpnews.feature.news.NewsViewModel
import com.example.kmpnews.feature.newsdetail.NewsDetailViewModel
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<UserSettingsRepository> { DefaultUserSettingsRepository() }
    single<ArticleRepository> { DefaultArticleRepository() }
    single<HttpClient> { createDefaultHttpClient() }

    viewModel { NewsViewModel(get()) }
    viewModel { parameters ->
        NewsDetailViewModel(
            articleId = parameters.get(),
            articleRepository = get(),
        )
    }
}
