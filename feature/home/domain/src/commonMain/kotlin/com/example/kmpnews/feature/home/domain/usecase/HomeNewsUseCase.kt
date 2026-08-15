package com.example.kmpnews.feature.home.domain.usecase

import com.example.kmpnews.core.domain.result.RestResult
import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto
import com.example.kmpnews.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

class HomeNewsUseCase(
    private val homeRepository: HomeRepository,
) {
    operator fun invoke(category: String): Flow<RestResult<List<NewsHomeArticleDto>>> =
        homeRepository.getHomeNews(category)
}
