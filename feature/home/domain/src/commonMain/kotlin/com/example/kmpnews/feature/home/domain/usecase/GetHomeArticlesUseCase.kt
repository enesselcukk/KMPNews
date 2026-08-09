package com.example.kmpnews.feature.home.domain.usecase

import com.example.kmpnews.core.model.Article
import com.example.kmpnews.core.domain.repository.ArticleRepository

class GetHomeArticlesUseCase(
    private val articleRepository: ArticleRepository,
) {
    operator fun invoke(): List<Article> = articleRepository.getArticles()
}