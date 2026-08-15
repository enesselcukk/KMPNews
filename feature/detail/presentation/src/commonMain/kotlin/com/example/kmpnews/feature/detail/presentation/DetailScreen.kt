package com.example.kmpnews.feature.detail.presentation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import com.example.kmpnews.core.designsystem.component.BackNavigationIconButton
import com.example.kmpnews.core.designsystem.component.LoadingIndicator
import com.example.kmpnews.core.designsystem.theme.SdhOnMediaOverlay
import com.example.kmpnews.core.model.Article
import com.example.kmpnews.feature.detail.contract.DetailContract
import com.example.kmpnews.feature.detail.presentation.generated.resources.Res
import com.example.kmpnews.feature.detail.presentation.generated.resources.detail_article
import com.example.kmpnews.feature.detail.presentation.generated.resources.detail_back
import com.example.kmpnews.feature.detail.presentation.generated.resources.detail_by_author
import com.example.kmpnews.feature.detail.presentation.generated.resources.detail_error_generic
import com.example.kmpnews.feature.detail.presentation.generated.resources.detail_read_more
import com.example.kmpnews.feature.detail.presentation.generated.resources.detail_retry
import com.example.kmpnews.feature.detail.presentation.ui.formatPublishedDate
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

private const val HeroAspectRatio = 16f / 9f

@Composable
fun DetailScreen(
    newsId: String,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = koinViewModel { parametersOf(newsId) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetailScreenContent(
        modifier = modifier,
        uiState = uiState,
        onAction = viewModel::onAction,
        onRetry = viewModel::retry,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailScreenContent(
    uiState: DetailContract.UiState,
    onAction: (DetailContract.Action) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.secondary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                ),
                navigationIcon = {
                    BackNavigationIconButton(
                        onClick = { onAction(DetailContract.Action.BackClicked) },
                        contentDescription = stringResource(Res.string.detail_back),
                    )
                },
                title = {
                    Text(
                        text = uiState.article?.title
                            ?: stringResource(Res.string.detail_article),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
            )
        },
    ) { paddingValues ->
        when {
            uiState.isLoading && uiState.article == null -> {
                LoadingIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                )
            }

            uiState.article == null -> {
                DetailErrorState(
                    message = uiState.errorMessage ?: stringResource(Res.string.detail_error_generic),
                    onRetry = onRetry,
                    onBack = { onAction(DetailContract.Action.BackClicked) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                )
            }

            else -> {
                val article = checkNotNull(uiState.article)
                DetailArticleContent(
                    article = article,
                    isRefreshing = uiState.isLoading,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                )
            }
        }
    }
}

@Composable
private fun DetailArticleContent(
    article: Article,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    val articleContent = article.content?.takeIf { it.isNotBlank() }

    LazyColumn(
        modifier = modifier.background(colors.background),
    ) {
        item {
            DetailHeroImage(
                imageUrl = article.imageUrl,
                sourceName = article.source.name,
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                DetailMetaRow(
                    sourceName = article.source.name,
                    publishedAt = formatPublishedDate(article.publishedAt),
                )

                Text(
                    text = article.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        lineHeight = 30.sp,
                    ),
                    color = colors.onBackground,
                )

                article.author?.takeIf { it.isNotBlank() }?.let { author ->
                    Text(
                        text = stringResource(Res.string.detail_by_author, author),
                        style = MaterialTheme.typography.labelMedium,
                        color = colors.onSurfaceVariant,
                    )
                }

                article.description?.takeIf { it.isNotBlank() }?.let { description ->
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.Medium,
                        ),
                        color = colors.onBackground,
                    )
                }

                if (isRefreshing) {
                    LoadingIndicator(modifier = Modifier.fillMaxWidth().height(48.dp))
                }
            }
        }

        if (articleContent != null) {
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = colors.surfaceVariant,
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = stringResource(Res.string.detail_read_more),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp,
                        ),
                        color = colors.primary,
                    )
                    Text(
                        text = articleContent,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 16.sp,
                            lineHeight = 26.sp,
                        ),
                        color = colors.onBackground,
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DetailHeroImage(
    imageUrl: String?,
    sourceName: String,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(HeroAspectRatio)
            .background(colors.surfaceVariant),
    ) {
        if (!imageUrl.isNullOrBlank()) {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        LoadingIndicator(modifier = Modifier.size(40.dp))
                    }
                },
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.72f),
                        ),
                    ),
                ),
        )

        Text(
            text = sourceName.uppercase(),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
            ),
            color = SdhOnMediaOverlay,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
        )
    }
}

@Composable
private fun DetailMetaRow(
    sourceName: String,
    publishedAt: String,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            shape = RoundedCornerShape(999.dp),
            color = colors.primary,
        ) {
            Text(
                text = sourceName.uppercase(),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = colors.onPrimary,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            )
        }

        Text(
            text = publishedAt,
            style = MaterialTheme.typography.labelMedium,
            color = colors.onSurfaceVariant,
        )
    }
}

@Composable
private fun DetailErrorState(
    message: String,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextButton(onClick = onRetry) {
            Text(
                text = stringResource(Res.string.detail_retry),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = colors.primary,
            )
        }

        TextButton(onClick = onBack) {
            Text(
                text = stringResource(Res.string.detail_back),
                style = MaterialTheme.typography.labelMedium,
                color = colors.secondary,
            )
        }
    }
}
