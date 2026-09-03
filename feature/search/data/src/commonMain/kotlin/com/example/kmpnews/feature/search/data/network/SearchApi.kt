package com.example.kmpnews.feature.search.data.network

import com.example.kmpnews.core.network.config.NewsApiConfig.API_KEY
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

class SearchApi(
    private val httpClient: HttpClient,
) {
    suspend fun searchArticles(query: String): HttpResponse =
        httpClient.get("everything") {
            parameter("q", query)
            parameter("sortBy", "relevancy")
            parameter("pageSize", PAGE_SIZE)
            parameter("page", "1")
            parameter("apiKey", API_KEY)
        }

    private companion object {
        const val PAGE_SIZE = "20"
    }
}
