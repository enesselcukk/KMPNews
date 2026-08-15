package com.example.kmpnews.feature.detail.contract

import com.example.kmpnews.core.navigation.NavigationCommand
import kotlinx.serialization.Serializable

@Serializable
data class DetailScreenDestination(
    val newsId: String,
) : NavigationCommand.Destination
