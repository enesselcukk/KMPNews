package com.example.kmpnews.feature.detail.data.mapper

import com.example.kmpnews.core.model.Article
import com.example.kmpnews.core.model.Source
import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto

internal fun NewsHomeArticleDto.toArticle(): Article {
    val cleanedDescription = description?.cleanNewsApiText()
    val cleanedContent = content
        ?.cleanNewsApiText()
        ?.removeDuplicatePrefix(cleanedDescription)
        ?.takeIf { it.isNotBlank() }

    return Article(
        id = url.orEmpty(),
        title = title.orEmpty(),
        description = cleanedDescription,
        content = cleanedContent,
        url = url.orEmpty(),
        imageUrl = urlToImage,
        publishedAt = publishedAt.orEmpty(),
        source = Source(
            id = name.orEmpty(),
            name = name.orEmpty(),
        ),
        author = author,
    )
}

internal fun List<NewsHomeArticleDto>.findArticleByUrl(articleUrl: String): Article? =
    firstOrNull { it.url == articleUrl }?.toArticle()

private fun String.cleanNewsApiText(): String =
    removeNewsApiTruncationMarker()
        .stripHtmlTags()
        .normalizeWhitespace()

private fun String.removeNewsApiTruncationMarker(): String =
    replace(Regex("\\[\\+\\d+ chars]$"), "")

private fun String.stripHtmlTags(): String =
    replace(Regex("<[^>]*>"), " ")
        .replace("&nbsp;", " ")
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&quot;", "\"")
        .replace("&#39;", "'")
        .replace(Regex("\\s+"), " ")

private fun String.normalizeWhitespace(): String = trim()

private fun String.removeDuplicatePrefix(prefix: String?): String {
    if (prefix.isNullOrBlank()) return this

    val contentText = trim()
    val prefixText = prefix.trim()
    if (contentText.length <= prefixText.length) return this

    return if (contentText.startsWith(prefixText, ignoreCase = true)) {
        contentText.substring(prefixText.length).trimStart()
    } else {
        this
    }
}
