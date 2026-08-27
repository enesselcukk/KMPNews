package com.example.kmpnews.desktop.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kmpnews.core.designsystem.component.SearchIconButton
import com.example.kmpnews.core.designsystem.theme.SdhOrange
import com.example.kmpnews.core.designsystem.theme.SdhNavy
import com.example.kmpnews.desktop.designsystem.RemoteImage

import com.example.kmpnews.feature.home.presentation.jvm.HomeArticleSnapshot

private const val HeroAspectRatio = 4f / 3f

@Composable
fun DesktopHomeScreen(
    state: HomeViewModelState,
    modifier: Modifier = Modifier,
) {
    val snapshot = state.snapshot
    val hasContent = snapshot.headlines.isNotEmpty() || snapshot.feed.isNotEmpty()
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier.background(colors.background),
    ) {
        HomeTopBar()

        Box(modifier = Modifier.weight(1f)) {
            when {
                snapshot.isError && !hasContent -> {
                    HomeMessageView(
                        text = snapshot.errorMessage ?: "An error occurred.",
                        actionTitle = "TRY AGAIN",
                        onAction = state::retry,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                else -> {
                    HomeContentView(
                        state = state,
                        showsInitialPlaceholder = snapshot.isLoading && !hasContent,
                        modifier = Modifier.fillMaxSize(),
                    )

                    if (snapshot.isLoading && !hasContent) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(colors.background.copy(alpha = 0.6f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(color = SdhOrange)
                        }
                    }
                }
            }

            if (snapshot.isRefreshing && hasContent) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    color = SdhOrange,
                )
            }
        }
    }
}

@Composable
private fun HomeTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SdhNavy)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SdhOrange.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "SDH",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = SdhOrange,
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "SDH",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            Text(
                text = "NEWS",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        SearchIconButton(
            onClick = { /* TODO: search */ },
            contentDescription = "Search",
            tint = Color.White,
        )
    }
}

@Composable
private fun HomeContentView(
    state: HomeViewModelState,
    showsInitialPlaceholder: Boolean,
    modifier: Modifier = Modifier,
) {
    val snapshot = state.snapshot
    val headlines = snapshot.headlines
    val feed = snapshot.feed
    val hero = headlines.firstOrNull() ?: feed.firstOrNull()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState, enabled = !showsInitialPlaceholder)
            .padding(bottom = 24.dp),
    ) {
        when {
            showsInitialPlaceholder -> HomeHeroPlaceholder()
            hero != null -> HomeHeroView(
                article = hero,
                relativeTime = state.relativeTime(hero.publishedAt),
                readTime = state.readTime(hero.title, hero.articleDescription),
                onTap = { state.openDetail(hero.url) },
            )
        }

        HomeCategoryTabs(
            categories = snapshot.categories,
            selectedCategory = snapshot.selectedCategory ?: "general",
            label = state::categoryLabel,
            onSelect = state::selectCategory,
        )

        Text(
            text = "FEATURED NEWS",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        )

        if (showsInitialPlaceholder) {
            repeat(4) {
                FeaturedNewsCardPlaceholder(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                )
            }
        } else {
            val featured = feed.filter { it.url != hero?.url }
            featured.forEach { article ->
                FeaturedNewsCard(
                    article = article,
                    relativeTime = state.relativeTime(article.publishedAt),
                    onTap = { state.openDetail(article.url) },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                )
            }
        }
    }
}

@Composable
private fun HomeHeroPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(HeroAspectRatio)
            .clip(RoundedCornerShape(0.dp)),
    ) {
        RemoteImage(
            url = null,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun HomeCategoryTabs(
    categories: List<String>,
    selectedCategory: String,
    label: (String) -> String,
    onSelect: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        categories.forEach { category ->
            val selected = category == selectedCategory
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onSelect(category) },
            ) {
                Text(
                    text = label(category),
                    fontSize = 12.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    letterSpacing = 0.6.sp,
                    color = if (selected) SdhOrange else MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .width(if (selected) 24.dp else 0.dp)
                        .height(2.dp)
                        .clip(RoundedCornerShape(50))
                        .background(if (selected) SdhOrange else Color.Transparent),
                )
            }
        }
    }
}

@Composable
private fun HomeHeroView(
    article: HomeArticleSnapshot,
    relativeTime: String,
    readTime: String,
    onTap: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(HeroAspectRatio)
            .clickable(onClick = onTap),
    ) {
        RemoteImage(
            url = article.imageUrl,
            contentDescription = article.title,
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Transparent,
                            0.45f to Color.Black.copy(alpha = 0.2f),
                            0.75f to Color.Black.copy(alpha = 0.65f),
                            1.0f to Color.Black.copy(alpha = 0.92f),
                        ),
                    ),
                ),
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = article.title.uppercase(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = relativeTime,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = SdhOrange,
                )
                Text(
                    text = readTime,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = SdhOrange,
                )
            }
        }
    }
}

@Composable
private fun FeaturedNewsCard(
    article: HomeArticleSnapshot,
    relativeTime: String,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(14.dp)
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .border(1.dp, colors.outlineVariant, shape)
            .background(colors.surface)
            .clickable(onClick = onTap)
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RemoteImage(
            url = article.imageUrl,
            contentDescription = article.title,
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(12.dp)),
        )

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = article.title.uppercase(),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = relativeTime,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = SdhOrange,
            )
        }
    }
}

@Composable
private fun FeaturedNewsCardPlaceholder(modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(14.dp)
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .border(1.dp, colors.outlineVariant, shape)
            .background(colors.surface)
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colors.outlineVariant.copy(alpha = 0.35f)),
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(colors.outlineVariant.copy(alpha = 0.35f)),
            )
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(colors.outlineVariant.copy(alpha = 0.25f)),
            )
        }
    }
}

@Composable
private fun HomeMessageView(
    text: String,
    actionTitle: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onAction) {
            Text(
                text = actionTitle,
                fontWeight = FontWeight.Bold,
                color = SdhOrange,
            )
        }
    }
}
