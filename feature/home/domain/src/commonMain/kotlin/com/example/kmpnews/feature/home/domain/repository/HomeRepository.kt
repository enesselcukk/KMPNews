package com.example.kmpnews.feature.home.domain.repository

import com.example.kmpnews.core.domain.result.RestResult
import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getHomeNews(): Flow<RestResult<List<NewsHomeArticleDto>>>
}
