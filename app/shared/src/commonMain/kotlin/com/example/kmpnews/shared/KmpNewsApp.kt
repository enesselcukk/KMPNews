package com.example.kmpnews.shared

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.example.kmpnews.core.designsystem.theme.KmpNewsTheme
import com.example.kmpnews.core.navigation.KmpNewsNavHost
import com.example.kmpnews.core.navigation.NavEntryProvider
import com.example.kmpnews.core.navigation.NavigationManager
import com.example.kmpnews.feature.home.contract.HomeScreenDestination
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun KmpNewsApp() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .build()
    }

    val features: List<NavEntryProvider> = remember { getKoin().getAll() }
    val navigationManager = remember { getKoin().get<NavigationManager>() }

    KmpNewsTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
            KmpNewsNavHost(
                modifier = Modifier.padding(paddingValues),
                startDestination = HomeScreenDestination,
                features = features,
                navigationManager = navigationManager,
            )
        }
    }
}
