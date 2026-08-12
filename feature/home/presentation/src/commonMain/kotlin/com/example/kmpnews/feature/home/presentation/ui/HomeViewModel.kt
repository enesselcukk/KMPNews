package com.example.kmpnews.feature.home.presentation.ui

import androidx.lifecycle.viewModelScope
import com.example.kmpnews.core.domain.result.RestResult
import com.example.kmpnews.core.navigation.NavigationManager
import com.example.kmpnews.core.presentation.CoreViewModel
import com.example.kmpnews.feature.detail.contract.DetailScreenDestination
import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto
import com.example.kmpnews.feature.home.domain.usecase.HomeNewsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeNewsUseCase: HomeNewsUseCase,
    private val navigationManager: NavigationManager
) : CoreViewModel(), HomeActions {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadNews()
    }

    fun onCategorySelected(category: String) {
        _uiState.update { state ->
            if (state is HomeUiState.Success) {
                state.copy(selectedCategory = category)
            } else {
                state
            }
        }
    }

    fun onBottomNavSelected(index: Int) {
        _uiState.update { state ->
            if (state is HomeUiState.Success) {
                state.copy(selectedBottomNav = index)
            } else {
                state
            }
        }
    }

    private fun loadNews() {
        viewModelScope.launch {
            safeFlowApiCall { homeNewsUseCase() }
                .collect { result ->
                    _uiState.value = when (result) {
                        is RestResult.Loading -> {
                            result.result?.let { toSuccess(it) } ?: HomeUiState.Loading
                        }

                        is RestResult.Success -> toSuccess(result.result)

                        is RestResult.Error -> {
                            result.result?.let { toSuccess(it) }
                                ?: HomeUiState.Error(
                                    message = result.error.message,
                                )
                        }
                    }
                }
        }
    }

    private fun toSuccess(articles: List<NewsHomeArticleDto>): HomeUiState.Success {
        val current = _uiState.value as? HomeUiState.Success
        return HomeUiState.Success(
            headlines = articles.take(HEADLINE_COUNT),
            feed = articles,
            categories = HomeUiState.defaultCategories,
            selectedCategory = current?.selectedCategory ?: HomeUiState.defaultCategories.first(),
            selectedBottomNav = current?.selectedBottomNav ?: 0,
        )
    }

    override fun navigateToDetail(newsId: String?) {
        if (newsId == null) return
        navigationManager.navigate(
            navigationCommand = DetailScreenDestination(
                newsId = newsId,
            )
        )
    }

    override fun retry() {
        TODO("Not yet implemented")
    }

    private companion object {
        const val HEADLINE_COUNT = 5
    }
}
