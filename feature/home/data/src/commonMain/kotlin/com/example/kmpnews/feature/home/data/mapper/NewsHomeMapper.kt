package com.example.kmpnews.feature.home.data.mapper

import com.example.kmpnews.feature.home.domain.model.ArticleDto
import com.example.kmpnews.feature.home.domain.model.ArticlesResponse
import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto

fun ArticlesResponse.toNewsHomeArticles(): List<NewsHomeArticleDto> =
    articles.map { it.toNewsHomeArticleDto() }

fun ArticleDto.toNewsHomeArticleDto(): NewsHomeArticleDto =
    NewsHomeArticleDto(
        name = source.name,
        author = author,
        title = title,
        description = description,
        content = content,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
    )
