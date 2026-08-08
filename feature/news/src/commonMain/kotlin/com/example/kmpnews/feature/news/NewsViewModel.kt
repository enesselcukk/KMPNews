package com.example.kmpnews.feature.news

import androidx.lifecycle.ViewModel
import com.example.kmpnews.core.model.Article
import com.example.kmpnews.core.model.ArticleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NewsViewModel(
    private val articleRepository: ArticleRepository,
) : ViewModel() {
    private val articlesState = MutableStateFlow(articleRepository.getArticles())

    val articles: StateFlow<List<Article>> = articlesState.asStateFlow()
}
