package com.example.kmpnews.feature.home.presentation.ui

import androidx.compose.runtime.Composable
import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto
import com.example.kmpnews.feature.home.presentation.generated.resources.Res
import com.example.kmpnews.feature.home.presentation.generated.resources.home_just_now
import com.example.kmpnews.feature.home.presentation.generated.resources.home_read_time_minutes
import com.example.kmpnews.feature.home.presentation.generated.resources.home_time_hours_ago
import com.example.kmpnews.feature.home.presentation.generated.resources.home_time_minutes_ago
import kotlinx.datetime.Instant
import org.jetbrains.compose.resources.stringResource
import kotlin.math.max
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun formatHomeRelativeTime(publishedAt: String?): String {
    if (publishedAt.isNullOrBlank()) {
        return stringResource(Res.string.home_just_now)
    }

    val minutesAgo = minutesSincePublished(publishedAt)
    if (minutesAgo == null) {
        return formatPublishedDateFallback(publishedAt)
    }

    return when {
        minutesAgo < 1 -> stringResource(Res.string.home_just_now)
        minutesAgo < 60 -> stringResource(Res.string.home_time_minutes_ago, minutesAgo.toInt())
        minutesAgo < 24 * 60 -> stringResource(Res.string.home_time_hours_ago, (minutesAgo / 60).toInt())
        else -> formatPublishedDateFallback(publishedAt)
    }
}

@Composable
fun formatHomeReadTime(article: NewsHomeArticleDto): String {
    val wordCount = (article.title.orEmpty() + " " + article.description.orEmpty())
        .trim()
        .split(Regex("\\s+"))
        .count { it.isNotBlank() }
    val minutes = max(1, wordCount / 200)
    return stringResource(Res.string.home_read_time_minutes, minutes)
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
