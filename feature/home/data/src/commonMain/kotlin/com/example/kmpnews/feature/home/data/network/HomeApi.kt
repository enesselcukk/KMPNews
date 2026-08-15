package com.example.kmpnews.feature.home.data.network

import com.example.kmpnews.core.network.config.NewsApiConfig.API_KEY
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

class HomeApi(
    private val httpClient: HttpClient,
) {
    suspend fun getTopHeadlines(category: String): HttpResponse =
        httpClient.get("top-headlines") {
            parameter("category", category)
            parameter("pageSize", MAX_PAGE_SIZE)
            parameter("page", "1")
            parameter("apiKey", API_KEY)
        }

    private companion object {
        const val MAX_PAGE_SIZE = "100"
    }
}
