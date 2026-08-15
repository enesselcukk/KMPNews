package com.example.kmpnews.feature.home.presentation.ui

import androidx.compose.runtime.Composable
import com.example.kmpnews.feature.home.domain.model.NewsHeadlineCategory
import com.example.kmpnews.feature.home.presentation.generated.resources.Res
import com.example.kmpnews.feature.home.presentation.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun homeCategoryLabel(categoryId: String): String = when (categoryId) {
    NewsHeadlineCategory.GENERAL -> stringResource(Res.string.category_general)
    NewsHeadlineCategory.BUSINESS -> stringResource(Res.string.category_business)
    NewsHeadlineCategory.ENTERTAINMENT -> stringResource(Res.string.category_entertainment)
    NewsHeadlineCategory.HEALTH -> stringResource(Res.string.category_health)
    NewsHeadlineCategory.SCIENCE -> stringResource(Res.string.category_science)
    NewsHeadlineCategory.SPORTS -> stringResource(Res.string.category_sports)
    NewsHeadlineCategory.TECHNOLOGY -> stringResource(Res.string.category_technology)
    else -> categoryId
}
