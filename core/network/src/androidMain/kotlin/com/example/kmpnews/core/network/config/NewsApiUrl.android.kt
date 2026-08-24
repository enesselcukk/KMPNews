package com.example.kmpnews.core.network.config

import io.ktor.http.URLProtocol

internal actual fun newsApiUrl(): NewsApiUrl = NewsApiUrl(
    protocol = URLProtocol.HTTPS,
    host = NewsApiConfig.HOST,
    basePath = NewsApiConfig.BASE_PATH,
)
