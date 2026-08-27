package com.example.kmpnews.desktop.features.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.kmpnews.feature.home.presentation.jvm.HomeUiStateSnapshot
import com.example.kmpnews.feature.home.presentation.jvm.HomeViewModelController
import com.example.kmpnews.feature.home.presentation.jvm.formatHomeReadTime
import com.example.kmpnews.feature.home.presentation.jvm.formatHomeRelativeTime
import com.example.kmpnews.feature.home.presentation.jvm.homeCategoryLabel

class HomeViewModelState(
    private val controller: HomeViewModelController,
) {
    var snapshot by mutableStateOf(controller.currentSnapshot())
        private set

    init {
        controller.observe { newSnapshot ->
            if (shouldPublish(snapshot, newSnapshot)) {
                snapshot = newSnapshot
            }
        }
    }

    fun selectCategory(category: String) {
        controller.onCategorySelected(category)
    }

    fun openDetail(url: String) {
        controller.navigateToDetail(url)
    }

    fun retry() {
        controller.retry()
    }

    fun categoryLabel(categoryId: String): String = homeCategoryLabel(categoryId)

    fun relativeTime(publishedAt: String?): String = formatHomeRelativeTime(publishedAt)

    fun readTime(title: String, description: String?): String = formatHomeReadTime(title, description)

    fun close() {
        controller.close()
    }

    private companion object {
        fun shouldPublish(old: HomeUiStateSnapshot, new: HomeUiStateSnapshot): Boolean =
            old.isLoading != new.isLoading ||
                old.isError != new.isError ||
                old.isRefreshing != new.isRefreshing ||
                old.selectedCategory != new.selectedCategory ||
                old.headlines.size != new.headlines.size ||
                old.feed.size != new.feed.size ||
                old.errorMessage != new.errorMessage ||
                old.headlines.firstOrNull()?.url != new.headlines.firstOrNull()?.url
    }
}

@Composable
fun rememberHomeViewModelState(): HomeViewModelState {
    val controller = remember { HomeViewModelController() }
    val state = remember(controller) { HomeViewModelState(controller) }

    DisposableEffect(controller) {
        onDispose { state.close() }
    }

    return state
}
