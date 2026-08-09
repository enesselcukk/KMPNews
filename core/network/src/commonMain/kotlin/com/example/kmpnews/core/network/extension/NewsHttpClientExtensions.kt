package com.example.kmpnews.core.network.extension

import com.example.kmpnews.core.network.result.NewsApiResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

suspend inline fun <reified T> HttpClient.safeNewsApiGet(path: String): NewsApiResult<T> =
    safeNewsApiCall { get(path).body<T>() }

suspend inline fun <T> safeNewsApiCall(block: suspend () -> T): NewsApiResult<T> =
    try {
        NewsApiResult.Success(block())
    } catch (exception: Exception) {
        NewsApiResult.Error(exception)
    }
