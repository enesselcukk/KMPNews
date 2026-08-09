package com.example.kmpnews.feature.home.domain.repository

import com.example.kmpnews.core.domain.result.RestResult
import com.example.kmpnews.feature.home.domain.model.ArticlesResponse
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getHomeNews(): Flow<RestResult<List<ArticlesResponse>>>
}