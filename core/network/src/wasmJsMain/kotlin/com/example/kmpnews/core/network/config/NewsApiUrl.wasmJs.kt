package com.example.kmpnews.core.network.config

import io.ktor.http.URLProtocol
import kotlinx.browser.window

internal actual fun newsApiUrl(): NewsApiUrl {
    val location = window.location
    val protocol = if (location.protocol == "https:") {
        URLProtocol.HTTPS
    } else {
        URLProtocol.HTTP
    }
    return NewsApiUrl(
        protocol = protocol,
        host = location.hostname,
        basePath = "/news-api/v2/",
        port = location.port.toIntOrNull(),
    )
}
