package com.example.kmpnews.feature.home.contract

import com.example.kmpnews.core.model.Article

object HomeContract {

    data class UiState(
        val articles: List<Article> = emptyList(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
    )

    sealed interface Action {
        data class ArticleClicked(val articleId: String) : Action
    }
}
