package com.example.kmpnews.feature.detail.data.mapper

import com.example.kmpnews.core.model.Article
import com.example.kmpnews.core.model.Source
import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto

internal fun NewsHomeArticleDto.toArticle(): Article =
    Article(
        id = url.orEmpty(),
        title = title.orEmpty(),
        description = description,
        content = content?.cleanNewsApiContent(),
        url = url.orEmpty(),
        imageUrl = urlToImage,
        publishedAt = publishedAt.orEmpty(),
        source = Source(
            id = name.orEmpty(),
            name = name.orEmpty(),
        ),
        author = author,
    )

internal fun List<NewsHomeArticleDto>.findArticleByUrl(articleUrl: String): Article? =
    firstOrNull { it.url == articleUrl }?.toArticle()

private fun String.cleanNewsApiContent(): String =
    replace(Regex("\\[\\+\\d+ chars]$"), "").trim()
