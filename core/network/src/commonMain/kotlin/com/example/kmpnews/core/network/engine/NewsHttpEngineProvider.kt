package com.example.kmpnews.core.network.engine

import io.ktor.client.engine.HttpClientEngineFactory

expect fun getProvide(): HttpClientEngineFactory<*>
