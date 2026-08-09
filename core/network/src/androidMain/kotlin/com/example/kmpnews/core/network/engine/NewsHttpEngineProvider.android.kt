package com.example.kmpnews.core.network.engine

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp

actual object NewsHttpEngineProvider {
    actual fun provide(): HttpClientEngineFactory<*> = OkHttp
}
