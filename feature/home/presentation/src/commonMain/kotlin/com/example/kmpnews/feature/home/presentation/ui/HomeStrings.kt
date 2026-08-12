package com.example.kmpnews.feature.home.presentation.ui

import androidx.compose.runtime.Composable
import com.example.kmpnews.feature.home.presentation.generated.resources.Res
import com.example.kmpnews.feature.home.presentation.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun homeCategoryLabel(categoryId: String): String = when (categoryId) {
    HomeCategories.ALL -> stringResource(Res.string.category_all)
    HomeCategories.NEWS -> stringResource(Res.string.category_news)
    HomeCategories.TECHNOLOGY -> stringResource(Res.string.category_technology)
    HomeCategories.ECONOMY -> stringResource(Res.string.category_economy)
    HomeCategories.SPORTS -> stringResource(Res.string.category_sports)
    HomeCategories.WORLD -> stringResource(Res.string.category_world)
    else -> categoryId
}
