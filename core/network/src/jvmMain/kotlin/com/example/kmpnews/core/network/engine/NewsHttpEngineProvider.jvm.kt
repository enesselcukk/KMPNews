package com.example.kmpnews.core.network.engine

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO

actual object NewsHttpEngineProvider {
    actual fun provide(): HttpClientEngineFactory<*> = CIO
}
