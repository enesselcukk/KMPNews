package com.example.kmpnews.feature.search.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kmpnews.core.designsystem.component.BackNavigationIconButton
import com.example.kmpnews.core.designsystem.component.LoadingIndicator
import com.example.kmpnews.feature.search.presentation.generated.resources.Res
import com.example.kmpnews.feature.search.presentation.generated.resources.search_back
import com.example.kmpnews.feature.search.presentation.generated.resources.search_error_generic
import com.example.kmpnews.feature.search.presentation.generated.resources.search_hint
import com.example.kmpnews.feature.search.presentation.generated.resources.search_idle_prompt
import com.example.kmpnews.feature.search.presentation.generated.resources.search_no_results
import com.example.kmpnews.feature.search.presentation.generated.resources.search_query_too_short
import com.example.kmpnews.feature.search.presentation.generated.resources.search_retry
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreenContent(
        uiState = uiState,
        onQueryChange = viewModel::onQueryChange,
        onSubmit = viewModel::onSubmit,
        onClearQuery = viewModel::onClearQuery,
        onResultSelected = viewModel::onResultSelected,
        onBack = viewModel::onBack,
        onRetry = viewModel::retry,
    )
}

@Composable
private fun SearchScreenContent(
    uiState: SearchUiState,
    onQueryChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onClearQuery: () -> Unit,
    onResultSelected: (String) -> Unit,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background),
    ) {
        SearchTopBar(
            query = uiState.query,
            onQueryChange = onQueryChange,
            onSubmit = onSubmit,
            onClearQuery = onClearQuery,
            onBack = onBack,
        )

        if (uiState.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        when {
            uiState.showIdlePrompt -> {
                SearchMessage(text = stringResource(Res.string.search_idle_prompt))
            }

            uiState.isQueryTooShort -> {
                SearchMessage(text = stringResource(Res.string.search_query_too_short))
            }

            uiState.errorMessage != null && uiState.results.isEmpty() -> {
                SearchErrorMessage(
                    message = uiState.errorMessage.orEmpty().ifBlank {
                        stringResource(Res.string.search_error_generic)
                    },
                    onRetry = onRetry,
                )
            }

            uiState.showEmptyResults -> {
                SearchMessage(
                    text = stringResource(Res.string.search_no_results, uiState.query.trim()),
                )
            }

            uiState.results.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        horizontal = 16.dp,
                        vertical = 12.dp,
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(
                        items = uiState.results,
                        key = { it.url },
                    ) { result ->
                        SearchResultCard(
                            result = result,
                            onClick = { onResultSelected(result.url) },
                        )
                    }
                }
            }

            uiState.isLoading -> {
                LoadingIndicator(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onClearQuery: () -> Unit,
    onBack: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.secondary)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BackNavigationIconButton(
            onClick = onBack,
            contentDescription = stringResource(Res.string.search_back),
            tint = colors.onSecondary,
        )

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            placeholder = {
                Text(stringResource(Res.string.search_hint))
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSubmit() }),
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = onClearQuery) {
                        Text(
                            text = "×",
                            color = colors.onSecondary,
                            fontSize = 22.sp,
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colors.surface,
                unfocusedContainerColor = colors.surface,
                focusedTextColor = colors.onSurface,
                unfocusedTextColor = colors.onSurface,
                cursorColor = colors.primary,
                focusedBorderColor = colors.primary,
            ),
        )
    }
}

@Composable
private fun SearchMessage(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(24.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun SearchErrorMessage(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onRetry) {
            Text(
                text = stringResource(Res.string.search_retry),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
