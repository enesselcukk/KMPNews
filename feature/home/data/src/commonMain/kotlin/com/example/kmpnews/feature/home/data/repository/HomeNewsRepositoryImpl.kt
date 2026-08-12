package com.example.kmpnews.feature.home.data.repository

import com.example.kmpnews.core.data.BaseRepository
import com.example.kmpnews.core.domain.result.RestResult
import com.example.kmpnews.feature.home.data.mapper.toNewsHomeArticles
import com.example.kmpnews.feature.home.data.network.HomeApi
import com.example.kmpnews.feature.home.domain.model.ArticlesResponse
import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto
import com.example.kmpnews.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

class HomeNewsRepositoryImpl(
    private val homeApi: HomeApi,
) : BaseRepository(), HomeRepository {

    override fun getHomeNews(): Flow<RestResult<List<NewsHomeArticleDto>>> =
        networkOnlyFlow(
            fetchFromNetwork = { homeApi.getNews() },
            mapToDomain = ArticlesResponse::toNewsHomeArticles,
        )
}
