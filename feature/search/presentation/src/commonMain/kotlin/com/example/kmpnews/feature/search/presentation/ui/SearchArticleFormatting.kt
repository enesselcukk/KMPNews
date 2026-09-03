package com.example.kmpnews.feature.search.presentation.ui

import androidx.compose.runtime.Composable
import com.example.kmpnews.feature.search.presentation.generated.resources.Res
import com.example.kmpnews.feature.search.presentation.generated.resources.search_just_now
import com.example.kmpnews.feature.search.presentation.generated.resources.search_time_hours_ago
import com.example.kmpnews.feature.search.presentation.generated.resources.search_time_minutes_ago
import kotlinx.datetime.Instant
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun formatSearchRelativeTime(publishedAt: String?): String {
    if (publishedAt.isNullOrBlank()) {
        return stringResource(Res.string.search_just_now)
    }

    val minutesAgo = minutesSincePublished(publishedAt)
    if (minutesAgo == null) {
        return formatPublishedDateFallback(publishedAt)
    }

    return when {
        minutesAgo < 1 -> stringResource(Res.string.search_just_now)
        minutesAgo < 60 -> stringResource(Res.string.search_time_minutes_ago, minutesAgo.toInt())
        minutesAgo < 24 * 60 -> stringResource(Res.string.search_time_hours_ago, (minutesAgo / 60).toInt())
        else -> formatPublishedDateFallback(publishedAt)
    }
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
