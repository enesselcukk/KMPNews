package com.example.kmpnews.feature.home.data.local

import com.example.kmpnews.core.database.dao.HomeHeadlineDao
import com.example.kmpnews.feature.home.data.mapper.toHomeHeadlineEntities
import com.example.kmpnews.feature.home.data.mapper.toNewsHomeArticleDto
import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto

class HomeNewsLocalDataSource(
    private val homeHeadlineDao: HomeHeadlineDao,
) {
    suspend fun getArticlesByCategory(category: String): List<NewsHomeArticleDto> =
        homeHeadlineDao.getByCategory(category).map { it.toNewsHomeArticleDto() }

    suspend fun saveArticles(category: String, articles: List<NewsHomeArticleDto>) {
        homeHeadlineDao.replaceCategoryArticles(
            category = category,
            articles = articles.toHomeHeadlineEntities(category),
        )
    }

    suspend fun findArticleByUrl(url: String): NewsHomeArticleDto? =
        homeHeadlineDao.findByUrl(url)?.toNewsHomeArticleDto()
}
