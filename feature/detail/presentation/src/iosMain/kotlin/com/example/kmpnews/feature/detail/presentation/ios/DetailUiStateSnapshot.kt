package com.example.kmpnews.feature.detail.presentation.ios

import com.example.kmpnews.feature.detail.contract.DetailContract

data class DetailUiStateSnapshot(
    val isLoading: Boolean,
    val errorMessage: String?,
    val title: String,
    val articleDescription: String?,
    val content: String?,
    val imageUrl: String?,
    val sourceName: String,
    val author: String?,
    val publishedAt: String,
)

fun DetailContract.UiState.toSnapshot(): DetailUiStateSnapshot {
    val article = article
    return DetailUiStateSnapshot(
        isLoading = isLoading && article == null,
        errorMessage = errorMessage,
        title = article?.title.orEmpty(),
        articleDescription = article?.description,
        content = article?.content,
        imageUrl = article?.imageUrl,
        sourceName = article?.source?.name.orEmpty(),
        author = article?.author,
        publishedAt = article?.publishedAt.orEmpty(),
    )
}
