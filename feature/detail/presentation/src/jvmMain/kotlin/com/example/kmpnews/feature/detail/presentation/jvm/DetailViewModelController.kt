package com.example.kmpnews.feature.detail.presentation.jvm

import com.example.kmpnews.feature.detail.contract.DetailContract
import com.example.kmpnews.feature.detail.presentation.DetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.mp.KoinPlatform.getKoin

class DetailViewModelController(
    articleUrl: String,
) {
    private val viewModel: DetailViewModel = getKoin().get { parametersOf(articleUrl) }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var observeJob: Job? = null

    fun currentSnapshot(): DetailUiStateSnapshot = viewModel.uiState.value.toSnapshot()

    fun observe(onChange: (DetailUiStateSnapshot) -> Unit) {
        observeJob?.cancel()
        observeJob = scope.launch {
            viewModel.uiState.collect { onChange(it.toSnapshot()) }
        }
    }

    fun onBackClicked() {
        viewModel.onAction(DetailContract.Action.BackClicked)
    }

    fun retry() {
        viewModel.retry()
    }

    fun close() {
        observeJob?.cancel()
        scope.cancel()
    }
}
