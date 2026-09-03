package com.example.kmpnews.feature.search.presentation.ui

import androidx.lifecycle.viewModelScope
import com.example.kmpnews.core.domain.result.RestResult
import com.example.kmpnews.core.navigation.NavigationCommand
import com.example.kmpnews.core.navigation.NavigationManager
import com.example.kmpnews.core.presentation.CoreViewModel
import com.example.kmpnews.feature.detail.contract.DetailScreenDestination
import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto
import com.example.kmpnews.feature.search.domain.usecase.SearchArticlesUseCase
import com.example.kmpnews.feature.search.presentation.ui.SearchUiState.Companion.DEBOUNCE_MS
import com.example.kmpnews.feature.search.presentation.ui.SearchUiState.Companion.MIN_QUERY_LENGTH
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val searchArticlesUseCase: SearchArticlesUseCase,
    private val navigationManager: NavigationManager,
) : CoreViewModel() {

    private val queryFlow = MutableStateFlow("")
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            queryFlow
                .debounce(DEBOUNCE_MS)
                .map { it.trim() }
                .distinctUntilChanged()
                .collectLatest { query -> executeSearch(query) }
        }
    }

    fun onQueryChange(query: String) {
        queryFlow.value = query
        val trimmed = query.trim()
        _uiState.update { state ->
            state.copy(
                query = query,
                errorMessage = null,
                isLoading = trimmed.length >= MIN_QUERY_LENGTH,
                hasSearched = trimmed.length >= MIN_QUERY_LENGTH && state.hasSearched,
            )
        }
    }

    fun onSubmit() {
        val query = queryFlow.value.trim()
        if (query.length < MIN_QUERY_LENGTH) return

        viewModelScope.launch {
            executeSearch(query)
        }
    }

    fun onClearQuery() {
        onQueryChange("")
        _uiState.value = SearchUiState()
    }

    fun onResultSelected(articleUrl: String) {
        if (articleUrl.isBlank()) return
        navigationManager.navigate(
            navigationCommand = NavigationCommand.NavigateTo(
                to = DetailScreenDestination(newsId = articleUrl),
            ),
        )
    }

    fun onBack() {
        navigationManager.navigate(NavigationCommand.NavigateUp)
    }

    fun retry() {
        val query = queryFlow.value.trim()
        if (query.length < MIN_QUERY_LENGTH) return

        viewModelScope.launch {
            executeSearch(query)
        }
    }

    private suspend fun executeSearch(query: String) {
        if (query.length < MIN_QUERY_LENGTH) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    results = emptyList(),
                    hasSearched = false,
                    errorMessage = null,
                )
            }
            return
        }

        safeFlowApiCall { searchArticlesUseCase(query) }
            .collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is RestResult.Loading -> state.copy(
                            isLoading = true,
                            errorMessage = null,
                        )

                        is RestResult.Success -> state.copy(
                            isLoading = false,
                            hasSearched = true,
                            results = result.result.map { it.toSearchResultItem() },
                            errorMessage = null,
                        )

                        is RestResult.Error -> state.copy(
                            isLoading = false,
                            hasSearched = true,
                            results = result.result?.map { it.toSearchResultItem() }.orEmpty(),
                            errorMessage = result.error.message,
                        )
                    }
                }
            }
    }

    private fun NewsHomeArticleDto.toSearchResultItem(): SearchResultItem =
        SearchResultItem(
            title = title.orEmpty(),
            url = url.orEmpty(),
            imageUrl = urlToImage,
            sourceName = name.orEmpty(),
            publishedAt = publishedAt,
            description = description,
        )
}
