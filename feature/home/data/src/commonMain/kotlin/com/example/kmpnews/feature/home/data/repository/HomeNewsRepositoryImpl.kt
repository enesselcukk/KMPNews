package com.example.kmpnews.feature.home.data.repository

import com.example.kmpnews.core.data.BaseRepository
import com.example.kmpnews.core.domain.result.RestResult
import com.example.kmpnews.feature.home.data.mapper.toNewsHomeArticles
import com.example.kmpnews.feature.home.data.network.HomeApi
import com.example.kmpnews.feature.home.domain.model.ArticlesResponse
import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto
import com.example.kmpnews.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class HomeNewsRepositoryImpl(
    private val homeApi: HomeApi,
) : BaseRepository(), HomeRepository {

    private val articleCache = mutableMapOf<String, NewsHomeArticleDto>()

    override fun getHomeNews(category: String): Flow<RestResult<List<NewsHomeArticleDto>>> =
        networkOnlyFlow(
            fetchFromNetwork = { homeApi.getTopHeadlines(category) },
            mapToDomain = ArticlesResponse::toNewsHomeArticles,
        ).onEach { result ->
            if (result is RestResult.Success) {
                cacheArticles(result.result)
            }
        }

    override fun findCachedArticleByUrl(url: String): NewsHomeArticleDto? =
        articleCache[url]

    private fun cacheArticles(articles: List<NewsHomeArticleDto>) {
        articles.forEach { article ->
            article.url?.let { url -> articleCache[url] = article }
        }
    }
}
