package com.example.kmpnews.shared

import androidx.compose.runtime.Composable
import com.example.kmpnews.core.designsystem.theme.KmpNewsTheme
import com.example.kmpnews.core.navigation.KmpNewsNavHost
import com.example.kmpnews.feature.news.NewsScreen
import com.example.kmpnews.feature.news.NewsViewModel
import com.example.kmpnews.feature.newsdetail.NewsDetailScreen
import com.example.kmpnews.feature.newsdetail.NewsDetailViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun KmpNewsApp() {
    KmpNewsTheme {
        KmpNewsNavHost(
            newsListContent = { onArticleClick ->
                NewsScreen(
                    onArticleClick = onArticleClick,
                    viewModel = koinViewModel<NewsViewModel>(),
                )
            },
            newsDetailContent = { articleId, onBackClick ->
                NewsDetailScreen(
                    articleId = articleId,
                    onBackClick = onBackClick,
                    viewModel = koinViewModel<NewsDetailViewModel> {
                        parametersOf(articleId)
                    },
                )
            },
        )
    }
}
