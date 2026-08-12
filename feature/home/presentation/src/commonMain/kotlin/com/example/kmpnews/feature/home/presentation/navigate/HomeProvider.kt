package com.example.kmpnews.feature.home.presentation.navigate

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.kmpnews.core.navigation.NavGraphProvider
import com.example.kmpnews.feature.home.contract.HomeScreenDestination
import com.example.kmpnews.feature.home.presentation.ui.HomeScreen

internal class HomeProvider : NavGraphProvider {
    override fun registerGraph(provider: NavGraphBuilder) {
        provider.apply {
            composable<HomeScreenDestination> {
                HomeScreen()
            }
        }
    }
}
