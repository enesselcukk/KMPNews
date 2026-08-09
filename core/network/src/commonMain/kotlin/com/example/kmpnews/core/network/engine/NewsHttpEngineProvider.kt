package com.example.kmpnews.core.network.engine

import io.ktor.client.engine.HttpClientEngineFactory

expect object NewsHttpEngineProvider {
    fun provide(): HttpClientEngineFactory<*>
}
