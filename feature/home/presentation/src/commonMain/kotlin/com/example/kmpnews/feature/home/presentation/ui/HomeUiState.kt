package com.example.kmpnews.feature.home.presentation.ui

import androidx.compose.runtime.Immutable
import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto

@Immutable
sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val headlines: List<NewsHomeArticleDto>,
        val feed: List<NewsHomeArticleDto>,
        val categories: List<String>,
        val selectedCategory: String,
        val selectedBottomNav: Int = 0,
    ) : HomeUiState

    data class Error(val message: String? = null) : HomeUiState

    companion object {
        val defaultCategories = HomeCategories.ids
    }
}
