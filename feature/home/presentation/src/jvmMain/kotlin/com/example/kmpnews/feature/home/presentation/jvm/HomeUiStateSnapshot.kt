package com.example.kmpnews.feature.home.presentation.jvm

import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto
import com.example.kmpnews.feature.home.presentation.ui.HomeUiState

data class HomeArticleSnapshot(
    val title: String,
    val url: String,
    val imageUrl: String?,
    val publishedAt: String?,
    val articleDescription: String?,
)

data class HomeUiStateSnapshot(
    val isLoading: Boolean,
    val isError: Boolean,
    val errorMessage: String?,
    val categories: List<String>,
    val selectedCategory: String?,
    val headlines: List<HomeArticleSnapshot>,
    val feed: List<HomeArticleSnapshot>,
    val isRefreshing: Boolean,
)

fun NewsHomeArticleDto.toSnapshot(): HomeArticleSnapshot = HomeArticleSnapshot(
    title = title.orEmpty(),
    url = url.orEmpty(),
    imageUrl = urlToImage,
    publishedAt = publishedAt,
    articleDescription = description,
)

fun HomeUiState.toSnapshot(): HomeUiStateSnapshot = when (this) {
    HomeUiState.Loading -> HomeUiStateSnapshot(
        isLoading = true,
        isError = false,
        errorMessage = null,
        categories = HomeUiState.defaultCategories,
        selectedCategory = null,
        headlines = emptyList(),
        feed = emptyList(),
        isRefreshing = false,
    )

    is HomeUiState.Success -> HomeUiStateSnapshot(
        isLoading = false,
        isError = false,
        errorMessage = null,
        categories = categories,
        selectedCategory = selectedCategory,
        headlines = headlines.map { it.toSnapshot() },
        feed = feed.map { it.toSnapshot() },
        isRefreshing = isRefreshing,
    )

    is HomeUiState.Error -> HomeUiStateSnapshot(
        isLoading = false,
        isError = true,
        errorMessage = message,
        categories = HomeUiState.defaultCategories,
        selectedCategory = null,
        headlines = emptyList(),
        feed = emptyList(),
        isRefreshing = false,
    )
}
