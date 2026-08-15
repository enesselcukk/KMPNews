package com.example.kmpnews.feature.detail.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailScreen(
    newsId: String,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = koinViewModel { parametersOf(newsId) },
) {
}
