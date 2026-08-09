package com.example.kmpnews.core.data.exception

class HttpStatusException(
    val statusCode: Int,
    message: String?,
) : Exception(message ?: "HTTP $statusCode")
