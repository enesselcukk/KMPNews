package com.example.kmpnews.desktop.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.example.kmpnews.core.designsystem.theme.KmpNewsTheme
import com.example.kmpnews.desktop.features.detail.DesktopDetailScreen
import com.example.kmpnews.desktop.features.home.DesktopHomeScreen
import com.example.kmpnews.desktop.features.home.rememberHomeViewModelState

@Composable
fun DesktopApp() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }.build()
    }

    val coordinator = remember { DesktopCoordinator() }
    val homeState = rememberHomeViewModelState()

    DisposableEffect(coordinator) {
        coordinator.start()
        onDispose { coordinator.close() }
    }

    KmpNewsTheme {
        val currentDetailUrl = coordinator.detailStack.lastOrNull()
        if (currentDetailUrl == null) {
            DesktopHomeScreen(
                state = homeState,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            DesktopDetailScreen(
                articleUrl = currentDetailUrl,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
