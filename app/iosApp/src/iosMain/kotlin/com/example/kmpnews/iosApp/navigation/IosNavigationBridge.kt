package com.example.kmpnews.iosApp.navigation

import com.example.kmpnews.core.navigation.NavigationCommand
import com.example.kmpnews.core.navigation.NavigationManager
import com.example.kmpnews.feature.detail.contract.DetailScreenDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin

class IosNavigationBridge {
    var onNavigateToDetail: ((String) -> Unit)? = null
    var onNavigateUp: (() -> Unit)? = null

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var observeJob: Job? = null

    fun start() {
        val navigationManager: NavigationManager = getKoin().get()
        observeJob?.cancel()
        observeJob = scope.launch {
            navigationManager.navigationCommandFlow.collect { command ->
                when (command) {
                    is NavigationCommand.NavigateTo -> {
                        val destination = command.to
                        if (destination is DetailScreenDestination) {
                            onNavigateToDetail?.invoke(destination.newsId)
                        }
                    }

                    NavigationCommand.NavigateUp -> onNavigateUp?.invoke()
                    else -> Unit
                }
            }
        }
    }

    fun close() {
        observeJob?.cancel()
        scope.cancel()
    }
}
