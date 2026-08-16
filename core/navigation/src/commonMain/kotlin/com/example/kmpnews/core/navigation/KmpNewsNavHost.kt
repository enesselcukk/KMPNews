package com.example.kmpnews.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule

@Composable
fun KmpNewsNavHost(
    startDestination: NavKey,
    features: List<NavEntryProvider>,
    navigationManager: NavigationManager,
    modifier: Modifier = Modifier,
) {
    val configuration = remember(features) {
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                features.forEach { include(it.navKeySerializers) }
            }
        }
    }
    val backStack = rememberNavBackStack(configuration, startDestination)

    LaunchedEffect(navigationManager) {
        navigationManager.navigationCommandFlow.collect { command ->
            backStack.execute(command)
        }
    }

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        transitionSpec = kmpNewsForwardTransitionSpec(),
        popTransitionSpec = kmpNewsPopTransitionSpec(),
        onBack = {
            backStack.execute(NavigationCommand.NavigateUp)
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            features.forEach { feature ->
                with(feature) { registerEntries() }
            }
        },
    )
}
