package com.example.kmpnews.feature.detail.domain

import com.example.kmpnews.core.model.Article
import com.example.kmpnews.core.domain.repository.ArticleRepository

class GetArticleDetailUseCase(
    private val articleRepository: ArticleRepository,
) {
    operator fun invoke(articleId: String): Article? = articleRepository.getArticle(articleId)
}
