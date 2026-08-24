package com.example.kmpnews.core.network.engine

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.js.Js

actual fun getProvide(): HttpClientEngineFactory<*> = Js
