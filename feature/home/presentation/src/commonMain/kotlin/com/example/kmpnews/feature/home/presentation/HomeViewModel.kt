package com.example.kmpnews.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmpnews.core.domain.result.RestResult
import com.example.kmpnews.feature.home.contract.HomeContract
import com.example.kmpnews.feature.home.domain.usecase.HomeNewsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getHomeArticlesUseCase: HomeNewsUseCase,
) : ViewModel() {
    private val uiState = MutableStateFlow(HomeContract.UiState(isLoading = true))

    val state: StateFlow<HomeContract.UiState> = uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getHomeArticlesUseCase().collect { result ->
                when (result) {
                    is RestResult.Loading -> {
                        uiState.update {
                            it.copy(
                                isLoading = true,
                                articles = result.result ?: it.articles,
                                errorMessage = null,
                            )
                        }
                    }

                    is RestResult.Success -> {
                        uiState.update {
                            it.copy(
                                isLoading = false,
                                articles = result.result,
                                errorMessage = null,
                            )
                        }
                    }

                    is RestResult.Error -> {
                        uiState.update {
                            it.copy(
                                isLoading = false,
                                articles = result.result ?: it.articles,
                                errorMessage = result.error.message,
                            )
                        }
                    }
                }
            }
        }
    }
}
