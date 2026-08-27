package com.example.kmpnews.desktop.features.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.kmpnews.feature.detail.presentation.jvm.DetailUiStateSnapshot
import com.example.kmpnews.feature.detail.presentation.jvm.DetailViewModelController
import com.example.kmpnews.feature.detail.presentation.jvm.formatPublishedDate

class DetailViewModelState(
    private val controller: DetailViewModelController,
) {
    var snapshot by mutableStateOf(controller.currentSnapshot())
        private set

    init {
        controller.observe { snapshot = it }
    }

    fun goBack() {
        controller.onBackClicked()
    }

    fun retry() {
        controller.retry()
    }

    fun formattedPublishedDate(): String = formatPublishedDate(snapshot.publishedAt)

    fun close() {
        controller.close()
    }
}

@Composable
fun rememberDetailViewModelState(articleUrl: String): DetailViewModelState {
    val controller = remember(articleUrl) { DetailViewModelController(articleUrl) }
    val state = remember(controller) { DetailViewModelState(controller) }

    DisposableEffect(controller) {
        onDispose { state.close() }
    }

    return state
}
