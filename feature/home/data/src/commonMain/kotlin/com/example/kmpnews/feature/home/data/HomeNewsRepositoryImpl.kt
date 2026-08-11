package com.example.kmpnews.feature.home.data

import com.example.kmpnews.core.data.BaseRepository
import com.example.kmpnews.core.domain.result.RestResult
import com.example.kmpnews.core.model.Article
import com.example.kmpnews.feature.home.data.mapper.toArticles
import com.example.kmpnews.feature.home.data.network.HomeApi
import com.example.kmpnews.feature.home.domain.model.ArticlesResponse
import com.example.kmpnews.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

class HomeNewsRepositoryImpl(
    private val homeApi: HomeApi,
) : BaseRepository(), HomeRepository {

    override fun getHomeNews(): Flow<RestResult<List<Article>>> =
        networkOnlyFlow<ArticlesResponse, List<Article>>(
            fetchFromNetwork = { homeApi.getNews() },
            mapToDomain = { response ->
                response.articles.toArticles()
            }
        )
}
