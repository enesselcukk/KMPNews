package com.example.kmpnews.feature.detail.presentation

import androidx.lifecycle.viewModelScope
import com.example.kmpnews.core.domain.result.RestResult
import com.example.kmpnews.core.navigation.NavigationCommand
import com.example.kmpnews.core.navigation.NavigationManager
import com.example.kmpnews.core.presentation.CoreViewModel
import com.example.kmpnews.feature.detail.contract.DetailContract
import com.example.kmpnews.feature.detail.domain.GetArticleDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val articleUrl: String,
    private val getArticleDetailUseCase: GetArticleDetailUseCase,
    private val navigationManager: NavigationManager,
) : CoreViewModel() {

    private val _uiState = MutableStateFlow(initialUiState())
    val uiState: StateFlow<DetailContract.UiState> = _uiState.asStateFlow()

    private fun initialUiState(): DetailContract.UiState {
        val cachedArticle = getArticleDetailUseCase.getCached(articleUrl)
        return if (cachedArticle != null) {
            DetailContract.UiState(article = cachedArticle, isLoading = false)
        } else {
            DetailContract.UiState(isLoading = true)
        }
    }

    init {
        loadArticle()
    }

    fun onAction(action: DetailContract.Action) {
        when (action) {
            DetailContract.Action.BackClicked -> navigationManager.navigate(NavigationCommand.NavigateUp)
        }
    }

    fun retry() {
        loadArticle()
    }

    private fun loadArticle() {
        viewModelScope.launch {
            safeFlowApiCall { getArticleDetailUseCase(articleUrl) }
                .collect { result ->
                    _uiState.value = when (result) {
                        is RestResult.Loading -> DetailContract.UiState(
                            article = result.result ?: _uiState.value.article,
                            isLoading = result.result == null,
                        )

                        is RestResult.Success -> DetailContract.UiState(
                            article = result.result,
                            isLoading = false,
                        )

                        is RestResult.Error -> DetailContract.UiState(
                            article = result.result,
                            isLoading = false,
                            errorMessage = result.error.message,
                        )
                    }
                }
        }
    }
}
