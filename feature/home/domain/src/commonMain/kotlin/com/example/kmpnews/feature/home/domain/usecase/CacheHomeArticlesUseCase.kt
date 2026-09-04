package com.example.kmpnews.feature.home.domain.usecase

import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto
import com.example.kmpnews.feature.home.domain.repository.HomeRepository

class CacheHomeArticlesUseCase(
    private val homeRepository: HomeRepository,
) {
    operator fun invoke(articles: List<NewsHomeArticleDto>) {
        if (articles.isEmpty()) return
        homeRepository.cacheArticles(articles)
    }
}
