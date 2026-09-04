package com.example.kmpnews.feature.home.data.repository

import com.example.kmpnews.core.data.BaseRepository
import com.example.kmpnews.core.domain.result.RestResult
import com.example.kmpnews.feature.home.data.local.HomeNewsLocalDataSource
import com.example.kmpnews.feature.home.data.mapper.toNewsHomeArticles
import com.example.kmpnews.feature.home.data.network.HomeApi
import com.example.kmpnews.feature.home.domain.model.ArticlesResponse
import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto
import com.example.kmpnews.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class HomeNewsRepositoryImpl(
    private val homeApi: HomeApi,
    private val localDataSource: HomeNewsLocalDataSource,
) : BaseRepository(), HomeRepository {

    private val articleCache = mutableMapOf<String, NewsHomeArticleDto>()

    override fun getHomeNews(category: String): Flow<RestResult<List<NewsHomeArticleDto>>> =
        offlineFirstFlow(
            fetchFromNetwork = { homeApi.getTopHeadlines(category) },
            saveToLocal = { response ->
                localDataSource.saveArticles(
                    category = category,
                    articles = response.toNewsHomeArticles(),
                )
            },
            readFromLocal = { localDataSource.getArticlesByCategory(category) },
            mapToDomain = { articles -> articles },
            mapNetworkToDomain = ArticlesResponse::toNewsHomeArticles,
            shouldFetch = { articles -> articles.isEmpty() },
        ).onEach { result ->
            val articles = when (result) {
                is RestResult.Loading -> result.result
                is RestResult.Success -> result.result
                is RestResult.Error -> result.result
            }
            if (articles != null) {
                cacheArticlesInternal(articles)
            }
        }

    override fun findCachedArticleByUrl(url: String): NewsHomeArticleDto? =
        articleCache[url]

    override fun cacheArticles(articles: List<NewsHomeArticleDto>) {
        cacheArticlesInternal(articles)
    }

    private fun cacheArticlesInternal(articles: List<NewsHomeArticleDto>) {
        articles.forEach { article ->
            article.url?.let { articleUrl -> articleCache[articleUrl] = article }
        }
    }
}
