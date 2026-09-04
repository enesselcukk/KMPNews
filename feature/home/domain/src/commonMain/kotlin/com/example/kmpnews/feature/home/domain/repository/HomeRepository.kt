package com.example.kmpnews.feature.home.domain.repository

import com.example.kmpnews.core.domain.result.RestResult
import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getHomeNews(category: String): Flow<RestResult<List<NewsHomeArticleDto>>>

    fun findCachedArticleByUrl(url: String): NewsHomeArticleDto?

    fun cacheArticles(articles: List<NewsHomeArticleDto>)
}
