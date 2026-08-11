package com.example.kmpnews.feature.home.domain.usecase

import com.example.kmpnews.core.domain.result.RestResult
import com.example.kmpnews.core.model.Article
import com.example.kmpnews.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

class HomeNewsUseCase(
    private val homeRepository: HomeRepository,
) {
    operator fun invoke(): Flow<RestResult<List<Article>>> = homeRepository.getHomeNews()
}
