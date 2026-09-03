package com.example.kmpnews.feature.search.presentation.ios

import com.example.kmpnews.feature.search.presentation.ui.SearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin

class SearchViewModelController {
    private val viewModel: SearchViewModel = getKoin().get()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var observeJob: Job? = null

    fun currentSnapshot(): SearchUiStateSnapshot = viewModel.uiState.value.toSnapshot()

    fun observe(onChange: (SearchUiStateSnapshot) -> Unit) {
        observeJob?.cancel()
        observeJob = scope.launch {
            viewModel.uiState.collect { onChange(it.toSnapshot()) }
        }
    }

    fun onQueryChange(query: String) {
        viewModel.onQueryChange(query)
    }

    fun onSubmit() {
        viewModel.onSubmit()
    }

    fun onClearQuery() {
        viewModel.onClearQuery()
    }

    fun onResultSelected(articleUrl: String) {
        viewModel.onResultSelected(articleUrl)
    }

    fun onBack() {
        viewModel.onBack()
    }

    fun retry() {
        viewModel.retry()
    }

    fun close() {
        observeJob?.cancel()
        scope.cancel()
    }
}
