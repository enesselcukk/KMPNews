package com.example.kmpnews.core.network.di

import com.example.kmpnews.core.network.client.NewsApiHttpClientFactory
import com.example.kmpnews.core.network.config.NewsApiConfig
import io.ktor.client.HttpClient
import org.koin.dsl.module

val newsApiModule = module {
    single<HttpClient> {
        NewsApiHttpClientFactory.create(
            baseUrl = NewsApiConfig.BASEURL,
            enableLogging = true)
    }
}
