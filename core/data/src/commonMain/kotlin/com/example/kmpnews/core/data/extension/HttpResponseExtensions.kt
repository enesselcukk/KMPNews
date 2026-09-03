package com.example.kmpnews.core.data.extension

import com.example.kmpnews.core.data.exception.HttpStatusException
import com.example.kmpnews.core.domain.result.RestResult
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

suspend inline fun <reified T : Any> HttpResponse.asRestResult(): RestResult<T> =
    if (status.isSuccess()) {
        RestResult.Success(body())
    } else {
        RestResult.Error(
            HttpStatusException(
                statusCode = status.value,
                message = status.description,
            )
        )
    }
