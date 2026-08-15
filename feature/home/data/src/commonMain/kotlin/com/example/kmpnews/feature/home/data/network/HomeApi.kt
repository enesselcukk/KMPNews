package com.example.kmpnews.feature.home.data.network

import com.example.kmpnews.core.network.config.NewsApiConfig.API_KEY
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

class HomeApi(
    private val httpClient: HttpClient,
) {
    suspend fun getNews(): HttpResponse =
        httpClient.get("everything") {
            parameter("q", "spor")
            parameter("language", "en")
            parameter("page", "1")
            parameter("apiKey", API_KEY)
        }
}
