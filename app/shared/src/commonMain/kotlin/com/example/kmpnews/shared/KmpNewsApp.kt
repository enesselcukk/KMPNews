package com.example.kmpnews.shared

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.kmpnews.core.designsystem.theme.KmpNewsTheme
import com.example.kmpnews.core.navigation.NavGraphProvider
import com.example.kmpnews.core.navigation.NavigationManager
import com.example.kmpnews.feature.home.contract.HomeScreenDestination
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun KmpNewsApp() {
    val navController = rememberNavController()

    val providers: List<NavGraphProvider> = getKoin().getAll()

    val navigationManager = getKoin().get<NavigationManager>()

    KmpNewsTheme {
        Scaffold(modifier = Modifier.fillMaxSize(), content = { paddingValues ->
            NavHost(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                startDestination = HomeScreenDestination
            ) {
                providers.forEach {
                    it.registerGraph(provider = this)
                }
            }

        })
    }

}
