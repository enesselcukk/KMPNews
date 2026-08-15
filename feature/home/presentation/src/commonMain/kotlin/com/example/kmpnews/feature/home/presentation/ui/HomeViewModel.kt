package com.example.kmpnews.feature.home.presentation.ui

import androidx.lifecycle.viewModelScope
import com.example.kmpnews.core.domain.result.RestResult
import com.example.kmpnews.core.navigation.NavigationCommand
import com.example.kmpnews.core.navigation.NavigationManager
import com.example.kmpnews.core.presentation.CoreViewModel
import com.example.kmpnews.feature.detail.contract.DetailScreenDestination
import com.example.kmpnews.feature.home.domain.model.NewsHeadlineCategory
import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto
import com.example.kmpnews.feature.home.domain.usecase.HomeNewsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeNewsUseCase: HomeNewsUseCase,
    private val navigationManager: NavigationManager,
) : CoreViewModel(), HomeActions {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var newsLoadJob: Job? = null

    init {
        loadNews(NewsHeadlineCategory.GENERAL)
    }

    fun onCategorySelected(category: String) {
        val current = _uiState.value
        if (current is HomeUiState.Success && current.selectedCategory == category) return

        _uiState.update { state ->
            when (state) {
                is HomeUiState.Success -> state.copy(selectedCategory = category)
                else -> HomeUiState.Loading
            }
        }
        loadNews(category)
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

    private fun loadNews(category: String) {
        newsLoadJob?.cancel()
        newsLoadJob = viewModelScope.launch {
            safeFlowApiCall { homeNewsUseCase(category) }
                .collect { result ->
                    _uiState.value = when (result) {
                        is RestResult.Loading -> {
                            result.result?.let { toSuccess(it, category) } ?: HomeUiState.Loading
                        }

                        is RestResult.Success -> toSuccess(result.result, category)

                        is RestResult.Error -> {
                            result.result?.let { toSuccess(it, category) }
                                ?: HomeUiState.Error(
                                    message = result.error.message,
                                )
                        }
                    }
                }
        }
    }

    private fun toSuccess(
        articles: List<NewsHomeArticleDto>,
        category: String,
    ): HomeUiState.Success {
        val current = _uiState.value as? HomeUiState.Success
        return HomeUiState.Success(
            headlines = articles.take(HEADLINE_COUNT),
            feed = articles,
            categories = HomeUiState.defaultCategories,
            selectedCategory = category,
            selectedBottomNav = current?.selectedBottomNav ?: 0,
        )
    }

    override fun navigateToDetail(newsId: String?) {
        if (newsId == null) return
        navigationManager.navigate(
            navigationCommand = NavigationCommand.NavigateTo(
                to = DetailScreenDestination(newsId = newsId),
            )
        )
    }

    override fun retry() {
        val category = (_uiState.value as? HomeUiState.Success)?.selectedCategory
            ?: NewsHeadlineCategory.GENERAL
        loadNews(category)
    }

    private companion object {
        const val HEADLINE_COUNT = 5
    }
}
