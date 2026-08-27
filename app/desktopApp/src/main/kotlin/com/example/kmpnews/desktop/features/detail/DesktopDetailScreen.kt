package com.example.kmpnews.desktop.features.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kmpnews.core.designsystem.theme.SdhNavy
import com.example.kmpnews.core.designsystem.theme.SdhOrange
import com.example.kmpnews.desktop.designsystem.RemoteImage

private const val HeroAspectRatio = 16f / 9f

@Composable
fun DesktopDetailScreen(
    articleUrl: String,
    modifier: Modifier = Modifier,
) {
    val state = rememberDetailViewModelState(articleUrl)
    val snapshot = state.snapshot
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier.background(colors.background),
    ) {
        DetailTopBar(onBack = state::goBack)

        when {
            snapshot.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = SdhOrange)
                }
            }

            snapshot.errorMessage != null && snapshot.title.isEmpty() -> {
                DetailErrorView(
                    message = snapshot.errorMessage ?: "Unable to load this article.",
                    onRetry = state::retry,
                    onBack = state::goBack,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            else -> {
                DetailContentView(
                    state = state,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun DetailTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(SdhNavy),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "‹",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier
                .size(44.dp)
                .clickable(onClick = onBack)
                .padding(start = 12.dp),
        )
    }
}

@Composable
private fun DetailContentView(
    state: DetailViewModelState,
    modifier: Modifier = Modifier,
) {
    val snapshot = state.snapshot
    val scrollState = rememberScrollState()
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier.verticalScroll(scrollState),
    ) {
        DetailHeroImage(
            imageUrl = snapshot.imageUrl,
            sourceName = snapshot.sourceName,
        )

        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = snapshot.sourceName.uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(SdhOrange)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                )
                Text(
                    text = state.formattedPublishedDate(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.onSurfaceVariant,
                )
            }

            Text(
                text = snapshot.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground,
            )

            snapshot.author?.takeIf { it.isNotBlank() }?.let { author ->
                Text(
                    text = "By $author",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.onSurfaceVariant,
                )
            }

            snapshot.articleDescription?.takeIf { it.isNotBlank() }?.let { description ->
                Text(
                    text = description,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.onBackground,
                    lineHeight = 22.sp,
                )
            }

            if (snapshot.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    color = SdhOrange,
                )
            }
        }

        snapshot.content?.takeIf { it.isNotBlank() }?.let { content ->
            HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))

            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Continue reading",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    color = SdhOrange,
                )
                Text(
                    text = content,
                    fontSize = 16.sp,
                    color = colors.onBackground,
                    lineHeight = 24.sp,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DetailHeroImage(
    imageUrl: String?,
    sourceName: String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(HeroAspectRatio),
    ) {
        RemoteImage(
            url = imageUrl,
            contentDescription = sourceName,
            modifier = Modifier.fillMaxSize(),
        )

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
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
        )
    }
}

@Composable
private fun DetailErrorView(
    message: String,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextButton(onClick = onRetry) {
            Text(
                text = "TRY AGAIN",
                fontWeight = FontWeight.Bold,
                color = SdhOrange,
            )
        }
        TextButton(onClick = onBack) {
            Text(
                text = "Go back",
                fontWeight = FontWeight.Medium,
                color = SdhNavy,
            )
        }
    }
}
