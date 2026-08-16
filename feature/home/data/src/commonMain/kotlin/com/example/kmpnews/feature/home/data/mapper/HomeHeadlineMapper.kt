package com.example.kmpnews.feature.home.data.mapper

import com.example.kmpnews.core.database.model.HomeHeadlineEntity
import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto

fun HomeHeadlineEntity.toNewsHomeArticleDto(): NewsHomeArticleDto =
    NewsHomeArticleDto(
        name = sourceName,
        author = author,
        title = title,
        description = description,
        url = articleUrl,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content,
    )

fun List<NewsHomeArticleDto>.toHomeHeadlineEntities(category: String): List<HomeHeadlineEntity> =
    mapIndexedNotNull { index, article ->
        article.url?.let { url ->
            HomeHeadlineEntity(
                category = category,
                articleUrl = url,
                sourceName = article.name,
                author = article.author,
                title = article.title,
                description = article.description,
                content = article.content,
                urlToImage = article.urlToImage,
                publishedAt = article.publishedAt,
                sortOrder = index,
            )
        }
    }
