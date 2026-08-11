package com.example.kmpnews.feature.home.data.mapper

import com.example.kmpnews.core.model.Article
import com.example.kmpnews.core.model.Source
import com.example.kmpnews.feature.home.domain.model.ArticleDto

internal fun ArticleDto.toArticle(): Article =
    Article(
        id = url,
        title = title,
        description = description,
        content = content,
        url = url,
        imageUrl = urlToImage,
        publishedAt = publishedAt,
        source = Source(
            id = source.id ?: source.name,
            name = source.name,
        ),
        author = author,
    )

internal fun List<ArticleDto>.toArticles(): List<Article> = map { it.toArticle() }
