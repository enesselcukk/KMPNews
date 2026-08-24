package com.example.kmpnews.core.network.config

import io.ktor.http.URLProtocol

internal data class NewsApiUrl(
    val protocol: URLProtocol,
    val host: String,
    val basePath: String,
    val port: Int? = null,
)

internal expect fun newsApiUrl(): NewsApiUrl
