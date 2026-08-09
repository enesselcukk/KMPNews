package com.example.kmpnews.shared

import androidx.compose.runtime.Composable
import com.example.kmpnews.core.designsystem.theme.KmpNewsTheme
import com.example.kmpnews.core.navigation.KmpNewsNavHost
import com.example.kmpnews.feature.detail.presentation.DetailScreen
import com.example.kmpnews.feature.detail.presentation.DetailViewModel
import com.example.kmpnews.feature.home.presentation.HomeScreen
import com.example.kmpnews.feature.home.presentation.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun KmpNewsApp() {
    KmpNewsTheme {
        KmpNewsNavHost(
            newsListContent = { onArticleClick ->
                HomeScreen(
                    onArticleClick = onArticleClick,
                    viewModel = koinViewModel<HomeViewModel>(),
                )
            },
            newsDetailContent = { articleId, onBackClick ->
                DetailScreen(
                    onBackClick = onBackClick,
                    viewModel = koinViewModel<DetailViewModel> {
                        parametersOf(articleId)
                    },
                )
            },
        )
    }
}
