package com.example.kmpnews.feature.search.domain.repository

import com.example.kmpnews.core.domain.result.RestResult
import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchArticles(query: String): Flow<RestResult<List<NewsHomeArticleDto>>>
}
