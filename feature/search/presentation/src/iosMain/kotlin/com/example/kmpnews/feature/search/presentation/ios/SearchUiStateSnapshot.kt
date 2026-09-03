package com.example.kmpnews.feature.search.presentation.ios

import com.example.kmpnews.feature.search.presentation.ui.SearchResultItem
import com.example.kmpnews.feature.search.presentation.ui.SearchUiState

data class SearchResultSnapshot(
    val title: String,
    val url: String,
    val imageUrl: String?,
    val sourceName: String,
    val publishedAt: String?,
    val description: String?,
)

data class SearchUiStateSnapshot(
    val query: String,
    val results: List<SearchResultSnapshot>,
    val isLoading: Boolean,
    val hasSearched: Boolean,
    val errorMessage: String?,
    val showIdlePrompt: Boolean,
    val isQueryTooShort: Boolean,
    val showEmptyResults: Boolean,
)

fun SearchResultItem.toSnapshot(): SearchResultSnapshot = SearchResultSnapshot(
    title = title,
    url = url,
    imageUrl = imageUrl,
    sourceName = sourceName,
    publishedAt = publishedAt,
    description = description,
)

fun SearchUiState.toSnapshot(): SearchUiStateSnapshot = SearchUiStateSnapshot(
    query = query,
    results = results.map { it.toSnapshot() },
    isLoading = isLoading,
    hasSearched = hasSearched,
    errorMessage = errorMessage,
    showIdlePrompt = showIdlePrompt,
    isQueryTooShort = isQueryTooShort,
    showEmptyResults = showEmptyResults,
)
