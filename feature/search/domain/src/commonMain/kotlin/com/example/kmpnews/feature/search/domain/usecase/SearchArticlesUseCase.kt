package com.example.kmpnews.feature.search.domain.usecase

import com.example.kmpnews.core.domain.result.RestResult
import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto
import com.example.kmpnews.feature.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchArticlesUseCase(
    private val searchRepository: SearchRepository,
) {
    operator fun invoke(query: String): Flow<RestResult<List<NewsHomeArticleDto>>> =
        searchRepository.searchArticles(query)
}
