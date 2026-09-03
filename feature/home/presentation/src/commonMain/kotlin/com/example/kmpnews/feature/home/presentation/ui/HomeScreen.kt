package com.example.kmpnews.feature.home.presentation.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import com.example.kmpnews.core.designsystem.component.SearchIconButton
import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto
import com.example.kmpnews.feature.home.presentation.generated.resources.Res
import com.example.kmpnews.feature.home.presentation.generated.resources.home_app_title
import com.example.kmpnews.feature.home.presentation.generated.resources.home_empty_news
import com.example.kmpnews.feature.home.presentation.generated.resources.home_error_generic
import com.example.kmpnews.feature.home.presentation.generated.resources.home_featured_news
import com.example.kmpnews.feature.home.presentation.generated.resources.home_news
import com.example.kmpnews.feature.home.presentation.generated.resources.home_search
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private const val HeroAspectRatio = 4f / 3f
private const val CATEGORY_CONTENT_ANIMATION_MS = 350

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
    ) {
        HomeTopBar(onSearchClick = viewModel::navigateToSearch)
        when (val state = uiState) {
            HomeUiState.Loading -> LoadingIndicator(modifier = Modifier.weight(1f))
            is HomeUiState.Error -> HomeMessage(
                text = state.message ?: stringResource(Res.string.home_error_generic),
                modifier = Modifier.weight(1f),
            )
            is HomeUiState.Success -> {
                Box(modifier = Modifier.weight(1f)) {
                    AnimatedContent(
                        targetState = state.content,
                        modifier = Modifier.fillMaxSize(),
                        contentKey = { it.category },
                        transitionSpec = {
                            (fadeIn(tween(CATEGORY_CONTENT_ANIMATION_MS)) +
                                slideInHorizontally(tween(CATEGORY_CONTENT_ANIMATION_MS)) { it / 5 }) togetherWith
                                (fadeOut(tween(CATEGORY_CONTENT_ANIMATION_MS)) +
                                    slideOutHorizontally(tween(CATEGORY_CONTENT_ANIMATION_MS)) { -it / 5 })
                        },
                        label = "home_category_content",
                    ) { content ->
                        when {
                            content.headlines.isEmpty() && content.feed.isEmpty() && state.isRefreshing -> {
                                HomeCategoryLoadingContent(modifier = Modifier.fillMaxSize())
                            }

                            content.headlines.isEmpty() && content.feed.isEmpty() -> {
                                HomeMessage(
                                    text = stringResource(Res.string.home_empty_news),
                                    modifier = Modifier.fillMaxSize(),
                                )
                            }

                            else -> {
                                HomeContent(
                                    categories = state.categories,
                                    selectedCategory = state.selectedCategory,
                                    onCategorySelected = viewModel::onCategorySelected,
                                    headlines = content.headlines,
                                    feed = content.feed,
                                    actions = viewModel,
                                    modifier = Modifier.fillMaxSize(),
                                )
                            }
                        }
                    }

                    if (state.isRefreshing) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopCenter),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeTopBar(
    onSearchClick: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.secondary)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(colors.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(Res.string.home_app_title),
                color = colors.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(Res.string.home_app_title),
                    color = colors.onSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(Res.string.home_news),
                    color = colors.onSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                )
            }
        }

        SearchIconButton(
            onClick = onSearchClick,
            contentDescription = stringResource(Res.string.home_search),
            tint = colors.onSecondary,
        )
    }
}

@Composable
private fun HomeCategoryTabs(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
) {
    val colors = MaterialTheme.colorScheme

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        items(categories) { category ->
            val selected = category == selectedCategory
            val indicatorWidth by animateDpAsState(
                targetValue = if (selected) 24.dp else 0.dp,
                animationSpec = tween(CATEGORY_CONTENT_ANIMATION_MS),
                label = "category_indicator_width",
            )
            Column(
                modifier = Modifier.clickable { onCategorySelected(category) },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = homeCategoryLabel(category),
                    color = if (selected) colors.primary else colors.onSurfaceVariant,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    style = MaterialTheme.typography.labelMedium,
                    letterSpacing = 0.6.sp,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .width(indicatorWidth)
                        .background(
                            color = colors.primary,
                            shape = RoundedCornerShape(2.dp),
                        ),
                )
            }
        }
    }
}

@Composable
private fun HomeContent(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    headlines: List<NewsHomeArticleDto>,
    feed: List<NewsHomeArticleDto>,
    actions: HomeActions,
    modifier: Modifier = Modifier,
) {
    if (headlines.isEmpty() && feed.isEmpty()) return

    val heroArticle = headlines.firstOrNull() ?: feed.first()
    val featuredArticles = feed.filter { it.url != heroArticle.url }

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 24.dp),
    ) {
        item {
            HomeHeroArticle(
                article = heroArticle,
                onClick = { actions.navigateToDetail(heroArticle.url.orEmpty()) },
            )
        }

        item {
            HomeCategoryTabs(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected,
            )
        }

        item {
            SectionTitle(title = stringResource(Res.string.home_featured_news))
        }

        items(
            items = featuredArticles,
            key = { article -> article.url.orEmpty() },
        ) { article ->
            FeaturedNewsCard(
                article = article,
                onClick = { actions.navigateToDetail(article.url.orEmpty()) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            )
        }
    }
}

@Composable
private fun HomeHeroArticle(
    article: NewsHomeArticleDto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(HeroAspectRatio)
            .clickable(onClick = onClick),
    ) {
        NewsArticleImage(
            imageUrl = article.urlToImage,
            contentDescription = article.title,
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.TopCenter,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(headlineScrimBrush(colors)),
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = article.title.orEmpty().uppercase(),
                color = colors.inverseOnSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HeroMetaText(text = formatHomeRelativeTime(article.publishedAt))
                HeroMetaText(text = formatHomeReadTime(article))
            }
        }
    }
}

@Composable
private fun HeroMetaText(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.labelMedium,
    )
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        letterSpacing = 1.sp,
    )
}

@Composable
private fun FeaturedNewsCard(
    article: NewsHomeArticleDto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(1.dp, colors.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            NewsArticleImage(
                imageUrl = article.urlToImage,
                contentDescription = article.title,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp)),
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = article.title.orEmpty().uppercase(),
                    color = colors.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = formatHomeRelativeTime(article.publishedAt),
                    color = colors.primary,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

@Composable
private fun HomeCategoryLoadingContent(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(HeroAspectRatio)
                    .background(colors.surfaceVariant),
            )
        }

        item {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                repeat(4) {
                    Box(
                        modifier = Modifier
                            .width(64.dp)
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(colors.surfaceVariant),
                    )
                }
            }
        }

        items(4) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.surface)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.surfaceVariant),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(colors.surfaceVariant),
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.45f)
                            .height(12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(colors.surfaceVariant),
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeMessage(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun NewsArticleImage(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    alignment: Alignment = Alignment.Center,
) {
    val colors = MaterialTheme.colorScheme
    val placeholderGradient = remember(contentDescription, colors) {
        headlinePlaceholderGradient(contentDescription.orEmpty(), colors)
    }

    if (imageUrl.isNullOrBlank()) {
        Box(modifier = modifier.background(placeholderGradient))
        return
    }

    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        alignment = alignment,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(placeholderGradient),
            )
        },
        error = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(placeholderGradient),
            )
        },
    )
}

private fun headlineScrimBrush(colors: ColorScheme): Brush = Brush.verticalGradient(
    colorStops = arrayOf(
        0.0f to Color.Transparent,
        0.45f to colors.scrim.copy(alpha = 0.2f),
        0.75f to colors.scrim.copy(alpha = 0.65f),
        1.0f to colors.scrim.copy(alpha = 0.92f),
    ),
)

private fun headlinePlaceholderGradient(
    seed: String,
    colors: ColorScheme,
): Brush {
    val palette = listOf(
        listOf(colors.primaryContainer, colors.primary),
        listOf(colors.secondaryContainer, colors.secondary),
        listOf(colors.tertiaryContainer, colors.tertiary),
        listOf(colors.surfaceVariant, colors.onSurfaceVariant),
    )
    val gradientColors = palette[kotlin.math.abs(seed.hashCode()) % palette.size]
    return Brush.linearGradient(gradientColors)
}
