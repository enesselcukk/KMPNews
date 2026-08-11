package com.example.kmpnews.feature.home.data.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

class HomeApi(
    private val httpClient: HttpClient,
) {
    suspend fun getNews(): HttpResponse =
        httpClient.get("everything") {
            parameter("q", "bitcoin")
            parameter("page", "1")
            parameter("apiKey", "1bb2021523d6486b8aef4c9e14aeb6a4")
        }
}
