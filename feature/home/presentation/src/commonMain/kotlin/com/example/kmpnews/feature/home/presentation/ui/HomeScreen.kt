package com.example.kmpnews.feature.home.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kmpnews.feature.home.domain.model.NewsHomeArticleDto
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier,
        containerColor = HomeScreenColors.Background,
        bottomBar = {
            when (val state = uiState) {
                is HomeUiState.Success -> {
                    HomeBottomBar(
                        selectedIndex = state.selectedBottomNav,
                        onItemSelected = viewModel::onBottomNavSelected,
                    )
                }

                else -> Unit
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            HomeTopBar()
            when (val state = uiState) {
                HomeUiState.Loading -> LoadingIndicator()
                is HomeUiState.Error -> HomeMessage(text = state.message)
                is HomeUiState.Success -> {
                    HomeCategoryTabs(
                        categories = state.categories,
                        selectedCategory = state.selectedCategory,
                        onCategorySelected = viewModel::onCategorySelected,
                    )
                    if (state.headlines.isEmpty() && state.feed.isEmpty()) {
                        HomeMessage(text = "Gösterilecek haber bulunamadı.")
                    } else {
                        HomeContent(
                            headlines = state.headlines,
                            feed = state.feed,
                            actions = viewModel,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(HomeScreenColors.Navy)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "SDH",
            color = HomeScreenColors.Orange,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = "SON DAKİKA",
                color = HomeScreenColors.TextOnDark,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                lineHeight = 14.sp,
            )
            Text(
                text = "HABER",
                color = HomeScreenColors.TextOnDark,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                lineHeight = 14.sp,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        HomeTopBarAction(label = "ARAMA", icon = "⌕")
        Spacer(modifier = Modifier.width(12.dp))
        HomeTopBarAction(label = "BİLDİRİMLER", icon = "🔔")
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(HomeScreenColors.Orange),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "E", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun HomeTopBarAction(label: String, icon: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = icon, color = HomeScreenColors.TextOnDark, fontSize = 18.sp)
        Text(
            text = label,
            color = HomeScreenColors.TextOnDark.copy(alpha = 0.85f),
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun HomeCategoryTabs(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        items(categories) { category ->
            val selected = category == selectedCategory
            Column(
                modifier = Modifier.clickable { onCategorySelected(category) },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = category,
                    color = if (selected) HomeScreenColors.Orange else HomeScreenColors.TextSecondary,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 13.sp,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .height(3.dp)
                        .width(if (selected) 28.dp else 0.dp)
                        .background(
                            color = HomeScreenColors.Orange,
                            shape = RoundedCornerShape(2.dp),
                        ),
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeContent(
    headlines: List<NewsHomeArticleDto>,
    feed: List<NewsHomeArticleDto>,
    actions: HomeActions,
) {
    if (headlines.isEmpty() && feed.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { headlines.size.coerceAtLeast(1) })

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        item {
            if (headlines.isNotEmpty()) {
                SectionTitle(title = "MANŞETLER")
                HorizontalPager(
                    state = pagerState,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    pageSpacing = 12.dp,
                ) { page ->
                    val article = headlines[page]
                    HeadlineCard(
                        article = article,
                        onClick = { actions.navigateToDetail(article.url.orEmpty())},
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    repeat(headlines.size) { index ->
                        val selected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 3.dp)
                                .size(if (selected) 8.dp else 6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (selected) HomeScreenColors.Orange
                                    else HomeScreenColors.TextSecondary.copy(alpha = 0.35f),
                                ),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
            SectionTitle(title = "GÜNCEL AKIŞ")
        }

        items(feed.size, key = { feed[it].url.orEmpty() }) { index ->
            val article = feed[index]
            FeedArticleCard(
                article = article,
                trailingIcon = when (index % 3) {
                    0 -> "🔖"
                    1 -> "↗"
                    else -> "♡"
                },
                onClick = { actions.navigateToDetail(article.url.orEmpty())},
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        color = HomeScreenColors.TextPrimary,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
    )
}

@Composable
private fun HeadlineCard(
    article: NewsHomeArticleDto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val gradient = remember(article.title) { headlineGradient(article.title.orEmpty()) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(gradient),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f)),
                        ),
                    )
                    .padding(16.dp),
            ) {
                Column {
                    Text(
                        text = article.title.orEmpty(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = article.description ?: article.publishedAt.orEmpty(),
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 11.sp,
                    )
                }
            }
        }
    }
}

@Composable
private fun FeedArticleCard(
    article: NewsHomeArticleDto,
    trailingIcon: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = HomeScreenColors.Card),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(headlineGradient(article.title.orEmpty())),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = article.title.orEmpty(),
                    color = HomeScreenColors.TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = article.publishedAt ?: "Az önce",
                    color = HomeScreenColors.TextSecondary,
                    fontSize = 12.sp,
                )
            }
            Text(text = trailingIcon, fontSize = 18.sp)
        }
    }
}

@Composable
private fun HomeBottomBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
) {
    val items = listOf(
        "ANA SAYFA" to "⌂",
        "KEŞFET" to "◎",
        "VİDEOLAR" to "▶",
        "PROFİL" to "☺",
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(HomeScreenColors.NavyDark)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        items.forEachIndexed { index, (label, icon) ->
            val selected = index == selectedIndex
            Column(
                modifier = Modifier.clickable { onItemSelected(index) },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = icon,
                    color = if (selected) HomeScreenColors.Orange else HomeScreenColors.TextOnDark,
                    fontSize = 18.sp,
                )
                Text(
                    text = label,
                    color = if (selected) HomeScreenColors.Orange else HomeScreenColors.TextOnDark,
                    fontSize = 10.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
private fun HomeMessage(text: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, color = HomeScreenColors.TextSecondary)
    }
}

private fun headlineGradient(seed: String): Brush {
    val palette = listOf(
        listOf(Color(0xFF455A64), Color(0xFF263238)),
        listOf(Color(0xFF5C6BC0), Color(0xFF3949AB)),
        listOf(Color(0xFF26A69A), Color(0xFF00897B)),
        listOf(Color(0xFF8D6E63), Color(0xFF5D4037)),
    )
    val colors = palette[kotlin.math.abs(seed.hashCode()) % palette.size]
    return Brush.linearGradient(colors)
}
