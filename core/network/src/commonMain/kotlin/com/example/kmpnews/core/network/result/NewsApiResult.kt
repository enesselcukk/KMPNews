package com.example.kmpnews.core.network.result

sealed interface NewsApiResult<out T> {
    data class Success<T>(val data: T) : NewsApiResult<T>

    data class Error(
        val exception: Throwable,
        val message: String = exception.message ?: "Unknown error",
    ) : NewsApiResult<Nothing>
}
