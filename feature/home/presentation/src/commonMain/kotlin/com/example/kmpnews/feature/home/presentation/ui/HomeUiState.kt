package com.example.kmpnews.feature.home.presentation.ui

import androidx.compose.runtime.Immutable
import com.example.kmpnews.feature.home.domain.model.NewsHeadlineCategory
import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto

@Immutable
data class HomeCategoryContent(
    val category: String,
    val headlines: List<NewsHomeArticleDto>,
    val feed: List<NewsHomeArticleDto>,
)

@Immutable
sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val content: HomeCategoryContent,
        val categories: List<String>,
        val isRefreshing: Boolean = false,
    ) : HomeUiState {
        val selectedCategory: String get() = content.category
        val headlines: List<NewsHomeArticleDto> get() = content.headlines
        val feed: List<NewsHomeArticleDto> get() = content.feed
    }

    data class Error(val message: String? = null) : HomeUiState

    companion object {
        val defaultCategories = NewsHeadlineCategory.all
    }
}
