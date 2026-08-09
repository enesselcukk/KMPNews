package com.example.kmpnews.feature.home.data

import com.example.kmpnews.core.data.BaseRepository
import com.example.kmpnews.core.model.Article
import com.example.kmpnews.core.domain.repository.ArticleRepository
import com.example.kmpnews.core.domain.result.RestResult
import com.example.kmpnews.core.model.sampleArticles
import com.example.kmpnews.feature.home.domain.model.ArticlesResponse
import com.example.kmpnews.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

class HomeArticleRepository : BaseRepository(), HomeRepository {
    override suspend fun getHomeNews(): Flow<RestResult<List<ArticlesResponse>>> {

    }

}
