package com.example.kmpnews.feature.detail.presentation

import androidx.lifecycle.ViewModel
import com.example.kmpnews.feature.detail.contract.DetailContract
import com.example.kmpnews.feature.detail.domain.GetArticleDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DetailViewModel(
    private val articleId: String,
) : ViewModel() {
    private val uiState = MutableStateFlow(
        DetailContract.UiState(article = getArticleDetailUseCase(articleId)),
    )

    val state: StateFlow<DetailContract.UiState> = uiState.asStateFlow()
}
