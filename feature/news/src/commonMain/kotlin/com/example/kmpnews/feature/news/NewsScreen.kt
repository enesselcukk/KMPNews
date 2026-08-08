package com.example.kmpnews.feature.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kmpnews.core.designsystem.component.NewsCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    onArticleClick: (String) -> Unit,
    viewModel: NewsViewModel,
    modifier: Modifier = Modifier,
) {
    val articles by viewModel.articles.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "KMP News",
                        style = MaterialTheme.typography.headlineSmall,
                    )
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(articles, key = { it.id }) { article ->
                NewsCard(
                    title = article.title,
                    description = article.description,
                    sourceName = article.source.name,
                    onClick = { onArticleClick(article.id) },
                )
            }
        }
    }
}
