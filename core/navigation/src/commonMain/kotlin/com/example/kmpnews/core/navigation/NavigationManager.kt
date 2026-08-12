package com.example.kmpnews.core.navigation

import com.spaceex.core.navigation.NavigationCommand
import kotlinx.coroutines.flow.Flow

interface NavigationManager {

    val navigationCommandFlow: Flow<NavigationCommand>

    fun navigate(navigationCommand: NavigationCommand)
}
