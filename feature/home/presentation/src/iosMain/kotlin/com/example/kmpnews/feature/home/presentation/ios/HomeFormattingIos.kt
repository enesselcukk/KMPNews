package com.example.kmpnews.feature.home.presentation.ios

import com.example.kmpnews.feature.home.domain.model.NewsHeadlineCategory
import kotlin.math.max
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.Instant

fun homeCategoryLabel(categoryId: String): String = when (categoryId) {
    NewsHeadlineCategory.GENERAL -> "GENERAL"
    NewsHeadlineCategory.BUSINESS -> "BUSINESS"
    NewsHeadlineCategory.ENTERTAINMENT -> "ENTERTAINMENT"
    NewsHeadlineCategory.HEALTH -> "HEALTH"
    NewsHeadlineCategory.SCIENCE -> "SCIENCE"
    NewsHeadlineCategory.SPORTS -> "SPORTS"
    NewsHeadlineCategory.TECHNOLOGY -> "TECHNOLOGY"
    else -> categoryId.uppercase()
}

fun formatHomeRelativeTime(publishedAt: String?): String {
    if (publishedAt.isNullOrBlank()) {
        return "Just now"
    }

    val minutesAgo = minutesSincePublished(publishedAt)
    if (minutesAgo == null) {
        return formatPublishedDateFallback(publishedAt)
    }

    return when {
        minutesAgo < 1 -> "Just now"
        minutesAgo < 60 -> "$minutesAgo min ago"
        minutesAgo < 24 * 60 -> "${minutesAgo / 60} hours ago"
        else -> formatPublishedDateFallback(publishedAt)
    }
}

fun formatHomeReadTime(title: String, description: String?): String {
    val wordCount = (title + " " + description.orEmpty())
        .trim()
        .split(Regex("\\s+"))
        .count { it.isNotBlank() }
    val minutes = max(1, wordCount / 200)
    return "$minutes min read"
}

@OptIn(ExperimentalTime::class)
private fun minutesSincePublished(publishedAt: String): Long? = runCatching {
    val publishedInstant = Instant.parse(publishedAt)
    val nowMillis = Clock.System.now().toEpochMilliseconds()
    (nowMillis - publishedInstant.toEpochMilliseconds()) / 60_000L
}.getOrNull()

private fun formatPublishedDateFallback(publishedAt: String): String {
    if (publishedAt.length < 10) return publishedAt

    val datePart = publishedAt.substring(0, 10)
    val parts = datePart.split("-")
    if (parts.size != 3) return publishedAt

    val (year, month, day) = parts
    return "$day.$month.$year"
}
