package com.example.kmpnews.feature.detail.data.repository

import com.example.kmpnews.core.data.BaseRepository
import com.example.kmpnews.core.domain.result.RestResult
import com.example.kmpnews.core.model.Article
import com.example.kmpnews.feature.detail.data.mapper.findArticleByUrl
import com.example.kmpnews.feature.detail.data.mapper.toArticle
import com.example.kmpnews.feature.detail.domain.ArticleNotFoundException
import com.example.kmpnews.feature.detail.domain.repository.DetailRepository
import com.example.kmpnews.feature.home.domain.model.NewsHeadlineCategory
import com.example.kmpnews.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class DetailRepositoryImpl(
    private val homeRepository: HomeRepository,
) : BaseRepository(), DetailRepository {

    override fun getArticleByUrl(articleUrl: String): Flow<RestResult<Article>> {
        homeRepository.findCachedArticleByUrl(articleUrl)?.let { cached ->
            return flow {
                emit(RestResult.Success(cached.toArticle()))
            }
        }

        return homeRepository.getHomeNews(NewsHeadlineCategory.GENERAL).map { result ->
            when (result) {
                is RestResult.Loading -> RestResult.Loading(
                    result = result.result?.findArticleByUrl(articleUrl),
                )

                is RestResult.Success -> {
                    val article = result.result.findArticleByUrl(articleUrl)
                    if (article != null) {
                        RestResult.Success(article)
                    } else {
                        RestResult.Error(ArticleNotFoundException(articleUrl))
                    }
                }

                is RestResult.Error -> RestResult.Error(
                    error = result.error,
                    result = result.result?.findArticleByUrl(articleUrl),
                )
            }
        }
    }
}
