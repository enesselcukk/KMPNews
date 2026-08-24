package com.example.kmpnews.core.network.client

import com.example.kmpnews.core.network.config.newsApiUrl
import com.example.kmpnews.core.network.engine.getProvide
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val TIMEOUT_MILLIS = 15_000L

object NewsApiHttpClientFactory {

    fun create(
        enableLogging: Boolean = true,
    ): HttpClient {
        return HttpClient(getProvide()) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        ignoreUnknownKeys = true
                        isLenient = true
                    },
                )
            }

            if (enableLogging) {
                install(Logging) {
                    level = LogLevel.ALL
                    logger = object : Logger {
                        override fun log(message: String) {
                            println("[NewsApiClient]: $message")
                        }
                    }
                }
            }

            install(HttpTimeout) {
                requestTimeoutMillis = TIMEOUT_MILLIS
                connectTimeoutMillis = TIMEOUT_MILLIS
                socketTimeoutMillis = TIMEOUT_MILLIS
            }

            defaultRequest {
                val endpoint = newsApiUrl()
                url {
                    protocol = endpoint.protocol
                    host = endpoint.host
                    endpoint.port?.let { port = it }
                    encodedPath = endpoint.basePath
                }
                contentType(ContentType.Application.Json)
            }
        }
    }
}
