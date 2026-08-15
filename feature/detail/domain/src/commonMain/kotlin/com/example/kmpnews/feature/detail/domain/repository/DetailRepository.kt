package com.example.kmpnews.feature.detail.domain.repository

import com.example.kmpnews.core.domain.result.RestResult
import com.example.kmpnews.core.model.Article
import kotlinx.coroutines.flow.Flow

interface DetailRepository {
    fun getArticleByUrl(articleUrl: String): Flow<RestResult<Article>>
}
