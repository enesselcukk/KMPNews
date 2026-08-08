package com.example.kmpnews.feature.newsdetail

import androidx.lifecycle.ViewModel
import com.example.kmpnews.core.model.Article
import com.example.kmpnews.core.model.ArticleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NewsDetailViewModel(
    private val articleId: String,
    private val articleRepository: ArticleRepository,
) : ViewModel() {
    private val articleState = MutableStateFlow(articleRepository.getArticle(articleId))

    val article: StateFlow<Article?> = articleState.asStateFlow()
}
