package com.example.kmpnews.feature.home.presentation.ui

import androidx.lifecycle.viewModelScope
import com.example.kmpnews.core.domain.result.RestResult
import com.example.kmpnews.core.navigation.NavigationCommand
import com.example.kmpnews.core.navigation.NavigationManager
import com.example.kmpnews.core.presentation.CoreViewModel
import com.example.kmpnews.feature.detail.contract.DetailScreenDestination
import com.example.kmpnews.feature.search.contract.SearchScreenDestination
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

    private val categoryArticlesCache = mutableMapOf<String, List<NewsHomeArticleDto>>()
    private var newsLoadJob: Job? = null

    init {
        loadNews(NewsHeadlineCategory.GENERAL)
    }

    fun onCategorySelected(category: String) {
        val current = _uiState.value
        if (current is HomeUiState.Success && current.selectedCategory == category) return

        _uiState.update { state ->
            when (state) {
                is HomeUiState.Success -> {
                    val cachedArticles = categoryArticlesCache[category]
                    buildSuccess(
                        articles = cachedArticles.orEmpty(),
                        category = category,
                        isRefreshing = cachedArticles == null,
                    )
                }

                else -> HomeUiState.Loading
            }
        }
        loadNews(category)
    }

    private fun loadNews(category: String) {
        newsLoadJob?.cancel()
        newsLoadJob = viewModelScope.launch {
            safeFlowApiCall { homeNewsUseCase(category) }
                .collect { result ->
                    val current = _uiState.value
                    _uiState.value = when (result) {
                        is RestResult.Loading -> {
                            when {
                                current is HomeUiState.Success && current.selectedCategory == category -> {
                                    current.copy(isRefreshing = true)
                                }

                                result.result != null -> {
                                    buildSuccess(
                                        articles = checkNotNull(result.result),
                                        category = category,
                                        isRefreshing = true,
                                    )
                                }

                                else -> HomeUiState.Loading
                            }
                        }

                        is RestResult.Success -> {
                            categoryArticlesCache[category] = result.result
                            buildSuccess(
                                articles = result.result,
                                category = category,
                                isRefreshing = false,
                            )
                        }

                        is RestResult.Error -> {
                            when {
                                current is HomeUiState.Success && current.selectedCategory == category -> {
                                    current.copy(isRefreshing = false)
                                }

                                result.result != null -> {
                                    buildSuccess(
                                        articles = checkNotNull(result.result),
                                        category = category,
                                        isRefreshing = false,
                                    )
                                }

                                else -> HomeUiState.Error(message = result.error.message)
                            }
                        }
                    }
                }
        }
    }

    private fun buildSuccess(
        articles: List<NewsHomeArticleDto>,
        category: String,
        isRefreshing: Boolean,
    ): HomeUiState.Success {
        return HomeUiState.Success(
            content = HomeCategoryContent(
                category = category,
                headlines = articles.take(HEADLINE_COUNT),
                feed = articles,
            ),
            categories = HomeUiState.defaultCategories,
            isRefreshing = isRefreshing,
        )
    }

    override fun navigateToDetail(newsId: String?) {
        if (newsId == null) return
        navigationManager.navigate(
            navigationCommand = NavigationCommand.NavigateTo(
                to = DetailScreenDestination(newsId = newsId),
            ),
        )
    }

    fun navigateToSearch() {
        navigationManager.navigate(
            navigationCommand = NavigationCommand.NavigateTo(
                to = SearchScreenDestination,
            ),
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
