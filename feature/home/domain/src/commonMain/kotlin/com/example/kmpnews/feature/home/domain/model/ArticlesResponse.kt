package com.example.kmpnews.feature.home.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ArticlesResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleDto>,
)
