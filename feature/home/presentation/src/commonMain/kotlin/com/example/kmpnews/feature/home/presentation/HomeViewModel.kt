package com.example.kmpnews.feature.home.presentation

import androidx.lifecycle.ViewModel
import com.example.kmpnews.feature.home.contract.HomeContract
import com.example.kmpnews.feature.home.domain.usecase.GetHomeArticlesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(
    private val getHomeArticlesUseCase: GetHomeArticlesUseCase,
) : ViewModel() {
    private val uiState = MutableStateFlow(
        HomeContract.UiState(articles = getHomeArticlesUseCase()),
    )

    val state: StateFlow<HomeContract.UiState> = uiState.asStateFlow()
}
