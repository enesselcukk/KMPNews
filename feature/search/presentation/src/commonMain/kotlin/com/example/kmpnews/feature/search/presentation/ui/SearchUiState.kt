package com.example.kmpnews.feature.search.presentation.ui

import androidx.compose.runtime.Immutable

data class SearchUiState(
    val query: String = "",
    val results: List<SearchResultItem> = emptyList(),
    val isLoading: Boolean = false,
    val hasSearched: Boolean = false,
    val errorMessage: String? = null,
) {
    val isQueryTooShort: Boolean
        get() = query.trim().length in 1 until MIN_QUERY_LENGTH

    val showIdlePrompt: Boolean
        get() = query.isBlank() && !hasSearched && !isLoading

    val showEmptyResults: Boolean
        get() = hasSearched && !isLoading && errorMessage == null && results.isEmpty() &&
            query.trim().length >= MIN_QUERY_LENGTH

    companion object {
        const val MIN_QUERY_LENGTH = 2
        const val DEBOUNCE_MS = 400L
    }
}

@Immutable
data class SearchResultItem(
    val title: String,
    val url: String,
    val imageUrl: String?,
    val sourceName: String,
    val publishedAt: String?,
    val description: String?,
)
