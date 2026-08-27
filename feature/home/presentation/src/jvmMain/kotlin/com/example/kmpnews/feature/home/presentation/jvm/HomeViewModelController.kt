package com.example.kmpnews.feature.home.presentation.jvm

import com.example.kmpnews.feature.home.presentation.ui.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin

class HomeViewModelController {
    private val viewModel: HomeViewModel = getKoin().get()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var observeJob: Job? = null

    fun currentSnapshot(): HomeUiStateSnapshot = viewModel.uiState.value.toSnapshot()

    fun observe(onChange: (HomeUiStateSnapshot) -> Unit) {
        observeJob?.cancel()
        observeJob = scope.launch {
            viewModel.uiState.collect { onChange(it.toSnapshot()) }
        }
    }

    fun onCategorySelected(category: String) {
        viewModel.onCategorySelected(category)
    }

    fun navigateToDetail(newsId: String) {
        viewModel.navigateToDetail(newsId)
    }

    fun retry() {
        viewModel.retry()
    }

    fun close() {
        observeJob?.cancel()
        scope.cancel()
    }
}
