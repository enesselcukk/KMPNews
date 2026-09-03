package com.example.kmpnews.feature.search.data.repository

import com.example.kmpnews.core.data.BaseRepository
import com.example.kmpnews.core.domain.result.RestResult
import com.example.kmpnews.feature.home.data.mapper.toNewsHomeArticles
import com.example.kmpnews.feature.home.domain.model.ArticlesResponse
import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto
import com.example.kmpnews.feature.search.data.network.SearchApi
import com.example.kmpnews.feature.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchRepositoryImpl(
    private val searchApi: SearchApi,
) : BaseRepository(), SearchRepository {

    override fun searchArticles(query: String): Flow<RestResult<List<NewsHomeArticleDto>>> =
        networkOnlyFlow(
            fetchFromNetwork = { searchApi.searchArticles(query) },
            mapToDomain = ArticlesResponse::toNewsHomeArticles,
        )
}
