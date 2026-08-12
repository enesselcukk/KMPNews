package com.example.kmpnews.feature.home.presentation.ui

internal interface HomeActions {
    fun navigateToDetail(newsId: String?)
    fun retry()
}
