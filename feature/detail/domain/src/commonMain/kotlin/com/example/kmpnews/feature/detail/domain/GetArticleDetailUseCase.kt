package com.example.kmpnews.feature.detail.domain

import com.example.kmpnews.core.domain.result.RestResult
import com.example.kmpnews.core.model.Article
import com.example.kmpnews.feature.detail.domain.repository.DetailRepository
import kotlinx.coroutines.flow.Flow

class GetArticleDetailUseCase(
    private val detailRepository: DetailRepository,
) {
    fun getCached(articleUrl: String): Article? =
        detailRepository.getCachedArticle(articleUrl)

    operator fun invoke(articleUrl: String): Flow<RestResult<Article>> =
        detailRepository.getArticleByUrl(articleUrl)
}
