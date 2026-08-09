package com.example.kmpnews.feature.detail.contract

import com.example.kmpnews.core.model.Article

object DetailContract {

    data class UiState(
        val article: Article? = null,
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
    )

    sealed interface Action {
        data object BackClicked : Action
    }
}
